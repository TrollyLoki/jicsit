package net.trollyloki.jicsit.server.https;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CertificateUtilsTest {

    private static CertificateFactory FACTORY;

    @BeforeAll
    static void beforeAll() throws CertificateException {
        FACTORY = CertificateFactory.getInstance("X.509");
    }

    @ParameterizedTest
    @CsvSource({
            "certificates/cert1.der, P6kXl6VhY3DQOI86BEJjUMr3eibgOEzKaiex07ADGM4="
    })
    void getFingerprint(String name, String expectedFingerprint) throws IOException, CertificateException {
        Certificate certificate;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(name)) {
            certificate = FACTORY.generateCertificate(stream);
        }
        assertEquals(expectedFingerprint, CertificateUtils.getFingerprint(certificate));
    }

}
