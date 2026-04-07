package net.trollyloki.jicsit.server.https;

import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A privilege level granted by an authentication token.
 */
@NullMarked
public enum PrivilegeLevel {

    /**
     * The client is not authenticated.
     */
    NOT_AUTHENTICATED("NotAuthenticated"),
    /**
     * The client is authenticated with Client privileges.
     */
    CLIENT("Client"),

    /**
     * The client is authenticated with Admin privileges.
     */
    ADMIN("Administrator"),

    /**
     * The client is authenticated with Initial Admin privileges to claim the server.
     */
    INITIAL_ADMIN("InitialAdmin"),

    /**
     * The client is authenticated as a third-party application.
     */
    API_TOKEN("APIToken");

    private final String value;

    PrivilegeLevel(String value) {
        this.value = value;
    }

    /**
     * Gets the string value used by the server for this privilege level.
     *
     * @return string value
     */
    public String value() {
        return value;
    }

    private static final Base64.Decoder TOKEN_DECODER = Base64.getDecoder();
    private static final JsonMapper TOKEN_MAPPER = new JsonMapper();

    private static final Map<String, PrivilegeLevel> VALUE_MAP = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(PrivilegeLevel::value, pl -> pl));

    /**
     * Determines the privilege level granted by a token.
     *
     * @param token authentication token
     * @return {@link PrivilegeLevel privilege level}
     * @throws IllegalArgumentException if the token is invalid
     */
    public static PrivilegeLevel ofToken(String token) {
        String[] split = token.split("\\.");
        if (split.length != 2) {
            throw new IllegalArgumentException("Tokens must consist of two parts separated by the dot character ('.')");
        }

        try {
            JsonNode tokenData = TOKEN_MAPPER.readTree(TOKEN_DECODER.decode(split[0]));

            JsonNode plNode = tokenData.get("pl");
            if (plNode == null || !plNode.isString()) {
                throw new IllegalArgumentException("Invalid token JSON");
            }
            String pl = plNode.asString();

            PrivilegeLevel level = VALUE_MAP.get(pl);
            if (level == null) {
                throw new IllegalArgumentException("Unknown privilege level: " + pl);
            }
            return level;

        } catch (JacksonException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
