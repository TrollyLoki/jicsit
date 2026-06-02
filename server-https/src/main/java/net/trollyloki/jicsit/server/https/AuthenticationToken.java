package net.trollyloki.jicsit.server.https;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.json.JsonMapper;

import java.util.Base64;

/**
 * A decoded authentication token.
 *
 * @param privilegeLevel {@link PrivilegeLevel privilege level} granted by the token
 * @param fingerprint    token fingerprint
 */
@NullMarked
public record AuthenticationToken(
        @JsonProperty("privilegeLevel") @JsonSerialize(using = PrivilegeLevel.Serializer.class) PrivilegeLevel privilegeLevel,
        @JsonProperty("authenticationToken") String fingerprint
) {

    private static final Base64.Decoder TOKEN_DECODER = Base64.getDecoder();
    private static final JsonMapper TOKEN_MAPPER = new JsonMapper();

    /**
     * Decodes an authentication token.
     *
     * @param token authentication token string
     * @return {@link AuthenticationToken decoded authentication token}
     * @throws IllegalArgumentException if the token is invalid
     */
    public static AuthenticationToken decode(String token) {
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

            return new AuthenticationToken(PrivilegeLevel.of(pl), split[1]);

        } catch (JacksonException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
