package net.trollyloki.jicsit.server.https;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrivilegeLevelTest {

    private void assertOfTokenThrows(String token) {
        assertThrows(IllegalArgumentException.class, () -> PrivilegeLevel.ofToken(token));
    }

    private void assertOfTokenEquals(PrivilegeLevel privilegeLevel, String token) {
        assertEquals(privilegeLevel, PrivilegeLevel.ofToken(token));
    }

    @Test
    void token_empty() {
        assertOfTokenThrows("");
    }

    @Test
    void token_random() {
        assertOfTokenThrows("0pGT*H^N&YQ#R%VIUYI");
    }

    @Test
    void token_malformed() {
        assertOfTokenThrows("abc.123");
    }

    @Test
    void token_emptyJson() {
        assertOfTokenThrows("e30=.a"); // encoded {}
    }

    @Test
    void token_wrongJsonType() {
        assertOfTokenThrows("eyJwbCI6dHJ1ZX0=.a"); // encoded {"pl":true}
    }

    @Test
    void token_invalidPrivilegeLevel() {
        assertOfTokenThrows("eyJwbCI6IiJ9.a"); // encoded {"pl":""}
    }

    @Test
    void token_notAuthenticated() {
        assertOfTokenEquals(PrivilegeLevel.NOT_AUTHENTICATED, "eyJwbCI6Ik5vdEF1dGhlbnRpY2F0ZWQifQ==.a"); // encoded {"pl":"NotAuthenticated"}
    }

    @Test
    void token_client() {
        assertOfTokenEquals(PrivilegeLevel.CLIENT, "eyJwbCI6IkNsaWVudCJ9.a"); // encoded {"pl":"Client"}
    }

    @Test
    void token_admin() {
        assertOfTokenEquals(PrivilegeLevel.ADMIN, "eyJwbCI6IkFkbWluaXN0cmF0b3IifQ==.a"); // encoded {"pl":"Administrator"}
    }

    @Test
    void token_initialAdmin() {
        assertOfTokenEquals(PrivilegeLevel.INITIAL_ADMIN, "eyJwbCI6IkluaXRpYWxBZG1pbiJ9.a"); // encoded {"pl":"InitialAdmin"}
    }

    @Test
    void token_apiToken() {
        assertOfTokenEquals(PrivilegeLevel.API_TOKEN, "eyJwbCI6IkFQSVRva2VuIn0=.a"); // encoded {"pl":"APIToken"}
    }

}
