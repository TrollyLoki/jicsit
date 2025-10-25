package net.trollyloki.jicsit.server.api.https.trustmanager;

import net.trollyloki.jicsit.server.api.https.CertificateUtils;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * A trust manager that only trusts certificates with a specific fingerprint.
 *
 * @see CertificateUtils#getFingerprint(Certificate)
 */
public class FingerprintBasedTrustManager extends X509ExtendedTrustManager {

    private final String fingerprint;

    /**
     * Creates a new fingerprint-based trust manager.
     *
     * @param fingerprint certificate fingerprint displayed by the game (without the "SHA256:" prefix)
     */
    public FingerprintBasedTrustManager(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    private void checkFingerprint(X509Certificate[] chain) throws CertificateException {
        String serverFingerprint = CertificateUtils.getFingerprint(chain[0]);
        if (!serverFingerprint.equals(fingerprint)) {
            throw new CertificateException("Incorrect server fingerprint: " + serverFingerprint);
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkFingerprint(chain);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkFingerprint(chain);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        checkFingerprint(chain);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        checkFingerprint(chain);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
        checkFingerprint(chain);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
        checkFingerprint(chain);
    }

}
