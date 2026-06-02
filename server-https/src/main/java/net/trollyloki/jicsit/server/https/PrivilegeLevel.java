package net.trollyloki.jicsit.server.https;

import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.util.Arrays;
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

    private static final Map<String, PrivilegeLevel> VALUE_MAP = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(PrivilegeLevel::value, pl -> pl));

    /**
     * Gets the {@link PrivilegeLevel} constant with the specified value.
     *
     * @param value value
     * @return {@link PrivilegeLevel privilege level}
     * @throws IllegalArgumentException if no such constant exists
     */
    public static PrivilegeLevel of(String value) {
        PrivilegeLevel constant = VALUE_MAP.get(value);
        if (constant == null) {
            throw new IllegalArgumentException("Unknown privilege level: " + value);
        }
        return constant;
    }

    /**
     * Determines the privilege level granted by a token.
     *
     * @param token authentication token
     * @return {@link PrivilegeLevel privilege level}
     * @throws IllegalArgumentException if the token is invalid
     * @see AuthenticationToken#decode(String)
     */
    public static PrivilegeLevel ofToken(String token) {
        return AuthenticationToken.decode(token).privilegeLevel();
    }

    static class Serializer extends ValueSerializer<PrivilegeLevel> {
        @Override
        public void serialize(PrivilegeLevel value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            gen.writeString(value.value);
        }
    }

}
