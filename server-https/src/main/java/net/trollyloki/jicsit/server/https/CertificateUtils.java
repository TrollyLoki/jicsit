package net.trollyloki.jicsit.server.https;

import net.trollyloki.jicsit.server.https.trustmanager.InsecureTrustManager;
import org.jspecify.annotations.NullMarked;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;

/**
 * Provides functions to obtain and fingerprint SSL certificates used by the game.
 */
@NullMarked
public final class CertificateUtils {
    private CertificateUtils() {
    }

    private static final String
            OPENSSL_PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----\n",
            OPENSSL_PUBLIC_KEY_FOOTER = "\n-----END PUBLIC KEY-----\n";
    private static final int OPENSSL_PEM_LINE_LENGTH = 64;

    /**
     * Encodes a public key into the PEM format used by OpenSSL.
     *
     * @param publicKey public key
     * @return PEM encoding in a string
     */
    private static String opensslPEMEncode(PublicKey publicKey) {
        String base64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        StringBuilder builder = new StringBuilder(
                OPENSSL_PUBLIC_KEY_HEADER.length() +
                        base64.length() + base64.length() / OPENSSL_PEM_LINE_LENGTH +
                        OPENSSL_PUBLIC_KEY_FOOTER.length()
        );

        builder.append(base64);
        for (int i = OPENSSL_PEM_LINE_LENGTH; i < builder.length(); i += OPENSSL_PEM_LINE_LENGTH) {
            builder.insert(i, "\n");
            i++;
        }
        builder.insert(0, OPENSSL_PUBLIC_KEY_HEADER);
        builder.append(OPENSSL_PUBLIC_KEY_FOOTER);

        return builder.toString();
    }

    /**
     * Gets the fingerprint for a certificate that is displayed by the game.
     *
     * @param certificate certificate
     * @return fingerprint
     * @throws UnsupportedOperationException if the SHA-256 algorithm is not available in the environment
     */
    public static String getFingerprint(Certificate certificate) {
        byte[] publicKeyPEM = opensslPEMEncode(certificate.getPublicKey()).getBytes(StandardCharsets.US_ASCII);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(digest.digest(publicKeyPEM));
        } catch (NoSuchAlgorithmException e) {
            // SHA-1 and SHA-256 support should be available on all platform implementations
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * Gets the fingerprint of a server that is displayed by the game.
     *
     * @param host server hostname
     * @param port server port
     * @return server fingerprint
     * @throws IOException                   if an I/O error occurs when connecting to the server
     * @throws UnknownHostException          if {@code host} is not known
     * @throws IllegalArgumentException      if {@code port} is invalid
     * @throws UnsupportedOperationException if the SHA-256 algorithm is not available in the environment
     */
    public static String getServerFingerprint(String host, int port) throws IOException {
        return getFingerprint(getServerCertificates(host, port)[0]);
    }

    /**
     * Retrieves a server's certificate(s).
     *
     * @param host server hostname
     * @param port server port
     * @return server certificate followed by any certificate authorities
     * @throws IOException              if an I/O error occurs when connecting to the server
     * @throws UnknownHostException     if {@code host} is not known
     * @throws IllegalArgumentException if {@code port} is invalid
     */
    private static Certificate[] getServerCertificates(String host, int port) throws IOException {
        try {

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new InsecureTrustManager()}, null);

            try (SSLSocket socket = (SSLSocket) sslContext.getSocketFactory().createSocket(host, port)) {
                return socket.getSession().getPeerCertificates();
            }

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            // these exceptions should never occur
            throw new RuntimeException(e);
        }
    }

}
