package net.trollyloki.jicsit.server.https.exception;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;

/**
 * An error returned by the dedicated server.
 */
@NullMarked
public class ApiException extends RuntimeException {

    /***/
    private final String errorCode;
    /***/
    private final @Nullable Map<String, Object> errorData;

    /**
     * Record of constructor arguments for convenient subclassing.
     *
     * @param errorCode    error code
     * @param errorMessage error message, possibly {@code null}
     * @param errorData    map of error data, possibly {@code null}
     * @see ApiException#ApiException(ApiException.ErrorResponse)
     */
    protected record ErrorResponse(
            String errorCode,
            @Nullable String errorMessage,
            @Nullable Map<String, Object> errorData
    ) {
    }

    /**
     * Creates a new API exception.
     *
     * @param response {@link ErrorResponse}
     */
    protected ApiException(ErrorResponse response) {
        super(response.errorMessage != null ? response.errorMessage : response.errorCode);
        this.errorCode = response.errorCode;
        this.errorData = response.errorData;
    }

    /**
     * Creates an instance of an API exception subclass appropriate for a specific error code.
     *
     * @param errorCode    error code
     * @param errorMessage error message, possibly {@code null}
     * @param errorData    map of error data, possibly {@code null}
     * @return API exception
     */
    public static ApiException createApiException(String errorCode, @Nullable String errorMessage, @Nullable Map<String, Object> errorData) {
        ErrorResponse response = new ErrorResponse(errorCode, errorMessage, errorData);
        return switch (errorCode) {
            case "invalid_token" -> new InvalidTokenException(response);
            case "insufficient_scope" -> new InsufficientPrivilegesException(response);
            case "passwordless_login_not_possible" -> new PasswordlessLoginNotPossibleException(response);
            case "wrong_password" -> new WrongPasswordException(response);
            case "server_claimed" -> new ServerAlreadyClaimedException(response);
            case "server_not_claimed" -> new ServerNotClaimedException(response);
            case "password_in_use" -> new PasswordInUseException(response);
            case "cannot_reset_admin_password" -> new CannotRemovePasswordException(response);
            case "save_game_failed", "file_save_failed" -> new SaveFailedException(response);
            case "delete_save_file_failed" -> new DeleteSaveFailedException(response);
            case "session_not_found" -> new SessionNotFoundException(response);
            case "delete_save_session_failed" -> new DeleteSessionFailedException(response);
            case "enumerate_sessions_failed" -> new EnumerateSessionsException(response);
            case "save_game_load_failed" -> new LoadFailedException(response);
            case "invalid_save_game" -> new InvalidSaveException(response);
            case "unsupported_save_game" -> new UnsupportedSaveException(response);
            case "file_not_found" -> new FileNotFoundException(response);
            default -> new ApiException(response);
        };
    }

    /**
     * Gets the error code returned by the server.
     *
     * @return error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the detailed error data provided by the server.
     *
     * @return map of error data, or {@code null} if none was provided
     */
    public @Nullable Map<String, Object> getErrorData() {
        return errorData;
    }

}
