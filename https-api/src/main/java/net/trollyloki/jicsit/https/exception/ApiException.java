package net.trollyloki.jicsit.https.exception;

import java.util.Collections;
import java.util.Map;

/**
 * An error returned by the dedicated server.
 */
public class ApiException extends RuntimeException {

    private final String errorCode;
    private final Map<String, Object> errorData;

    /**
     * Creates a new API exception.
     *
     * @param message   optional error message
     * @param errorCode error code
     * @param errorData map of error data, or {@code null}
     */
    public ApiException(String message, String errorCode, Map<String, Object> errorData) {
        super(message + " (" + errorCode + ")");
        this.errorCode = errorCode;
        this.errorData = errorData == null ? null : Collections.unmodifiableMap(errorData);
    }

    /**
     * Record of constructor arguments for convenient subclassing.
     *
     * @param errorCode    optional error message
     * @param errorMessage error code
     * @param errorData    map of error data, or {@code null}
     * @see ApiException#ApiException(ApiException.ErrorResponse)
     */
    protected record ErrorResponse(String errorCode, String errorMessage, Map<String, Object> errorData) {

    }

    /**
     * Creates a new API exception from an error response record.
     *
     * @param response {@link ErrorResponse}
     */
    protected ApiException(ErrorResponse response) {
        this(response.errorMessage, response.errorCode, response.errorData);
    }

    /**
     * Creates an instance of an API exception subclass appropriate for the error code.
     *
     * @param errorCode    error code
     * @param errorMessage optional error message
     * @param errorData    map of error data, or {@code null}
     * @return API exception
     */
    public static ApiException createApiException(String errorCode, String errorMessage, Map<String, Object> errorData) {
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
    public Map<String, Object> getErrorData() {
        return errorData;
    }

}
