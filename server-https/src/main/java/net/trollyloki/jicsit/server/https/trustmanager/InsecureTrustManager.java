package net.trollyloki.jicsit.server.https.trustmanager;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.X509Certificate;

/**
 * An insecure trust manager that accepts all certificates.
 */
public class InsecureTrustManager extends X509ExtendedTrustManager {

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
        // do not verify certificate chain
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
        // do not verify certificate chain
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {
        // do not verify certificate chain
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {
        // do not verify certificate chain
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
        // do not verify certificate chain
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
        // do not verify certificate chain
    }

}
