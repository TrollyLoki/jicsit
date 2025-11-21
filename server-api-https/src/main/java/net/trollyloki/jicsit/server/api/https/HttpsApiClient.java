package net.trollyloki.jicsit.server.api.https;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.trollyloki.jicsit.server.api.https.exception.ApiException;
import net.trollyloki.jicsit.server.api.https.trustmanager.FingerprintBasedTrustManager;
import net.trollyloki.jicsit.server.api.https.trustmanager.InsecureTrustManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * A client for the dedicated server HTTPS API.
 */
public class HttpsApiClient {

    private final RestClient client;
    private String token;

    private record ResponseSchema<T>(T data, String errorCode, String errorMessage, Map<String, Object> errorData) {

        private ApiException createApiException() {
            return ApiException.createApiException(errorCode, errorMessage, errorData);
        }

    }

    private final ObjectMapper errorJsonMapper = Jackson2ObjectMapperBuilder.json().build();

    private void handleError(HttpRequest request, ClientHttpResponse response) throws IOException {
        throw errorJsonMapper.readValue(response.getBody(), ResponseSchema.class).createApiException();
    }

    /**
     * Creates a new client.
     * <p>
     * The client is initially unauthenticated.
     * An API token for authentication can be provided by calling {@link #setToken(String)} on the created client.
     * Authentication can also be obtained by calling {@link HttpsApi#passwordlessLogin(PrivilegeLevel)}
     * or {@link HttpsApi#passwordLogin(PrivilegeLevel, String)}, but usage of these functions by third-party applications is discouraged by the developers.
     * <p>
     * The following custom trust manager implementations may be helpful
     * when attempting to connect to servers that are using self-signed certificates:
     * <ul>
     *     <li>
     *         {@link FingerprintBasedTrustManager}
     *         can be created with the fingerprint displayed by the game
     *         and will verify that the server's certificate matches it when connecting.
     *     </li>
     *     <li>
     *         {@link InsecureTrustManager}
     *         simply accepts any certificate presented by the server.
     *         <strong>This is not secure</strong> but could be useful when connecting to unknown servers.
     *     </li>
     * </ul>
     *
     * @param host         server host name
     * @param port         server port
     * @param trustManager custom trust manager to use, or {@code null} to use the default trust manager
     * @throws URISyntaxException if the constructed HTTPS URI is invalid
     */
    public HttpsApiClient(String host, int port, TrustManager trustManager) throws URISyntaxException {

        RestClient.Builder builder = RestClient.builder().requestInterceptor((request, body, execution) -> {
            // without this interceptor the requests don't include a content length header for some reason
            return execution.execute(request, body);
        });

        // certificate handling
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManager == null ? null : new TrustManager[]{trustManager}, null);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e);
        }
        builder.requestFactory(new JdkClientHttpRequestFactory(HttpClient.newBuilder().sslContext(sslContext).build()));

        // error handling
        builder.defaultStatusHandler(HttpStatusCode::isError, this::handleError);

        // server configuration
        builder.baseUrl(new URI("https", null, host, port, "/api/v1", null, null));

        this.client = builder.build();
    }

    /**
     * Creates a new client using the default trust manager.
     * Note that this means the client will <strong>refuse to connect to servers using self-signed certificates</strong>.
     * If you need to connect to a server that is using a self-signed certificate,
     * provide a custom trust manager as explained by {@link #HttpsApiClient(String, int, TrustManager)}
     *
     * @param host server host name
     * @param port server port
     * @throws URISyntaxException if the constructed HTTPS URI is invalid
     * @see #HttpsApiClient(String, int, TrustManager)
     */
    public HttpsApiClient(String host, int port) throws URISyntaxException {
        this(host, port, null);
    }

    /**
     * Sets the current authentication token used for all requests.
     *
     * @param token authentication token, or {@code null} to make unauthenticated requests
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the privilege level granted by the current authentication token.
     * <p>
     * {@link PrivilegeLevel#API_TOKEN} allows the client to execute all functions.
     *
     * @return {@link PrivilegeLevel}, {@link PrivilegeLevel#NOT_AUTHENTICATED} if no token is set
     * @throws IllegalArgumentException if the current authentication token is invalid
     */
    public PrivilegeLevel getPrivilegeLevel() {
        if (token == null) return PrivilegeLevel.NOT_AUTHENTICATED;
        return PrivilegeLevel.ofToken(token);
    }

    private Map<String, Object> createRequestBody(String function, Object functionData) {
        if (functionData == null) {
            return Map.of("function", function);
        } else {
            return Map.of("function", function, "data", functionData);
        }
    }

    private RestClient.RequestBodySpec createRequest() {
        RestClient.RequestBodySpec request = client.post();
        if (token != null) {
            request.headers(headers -> headers.setBearerAuth(token));
        }
        return request;
    }

    private RestClient.RequestBodySpec createRequest(String function, Object functionData) {
        return createRequest().contentType(MediaType.APPLICATION_JSON).body(createRequestBody(function, functionData));
    }

    private RestClient.RequestBodySpec createMultipartRequest(String function, Object functionData, String partName, InputStream partData) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("data", createRequestBody(function, functionData));
        parts.add(partName, new InputStreamResource(partData));

        return createRequest().contentType(MediaType.MULTIPART_FORM_DATA).body(parts);
    }

    private <T> T readResponse(RestClient.RequestBodySpec request, ParameterizedTypeReference<T> bodyType) {
        try {
            return request.retrieve().body(bodyType);
        } catch (RestClientException e) {
            throw new RequestException(e.getMessage(), e.getCause());
        }
    }

    private InputStream getRawResponse(RestClient.RequestBodySpec request) {
        try {
            return request.exchange((httpRequest, response) -> {
                if (response.getStatusCode().isError()) {
                    handleError(httpRequest, response);
                }
                return response.getBody();
            }, false);
        } catch (RestClientException e) {
            throw new RequestException(e.getMessage(), e.getCause());
        }
    }

    private <R> R parseResponse(RestClient.RequestBodySpec request, Class<R> responseDataType) {
        ParameterizedType responseType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{responseDataType};
            }

            @Override
            public Type getRawType() {
                return ResponseSchema.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        ResponseSchema<R> response = readResponse(request, ParameterizedTypeReference.forType(responseType));
        if (response == null) return null;

        // the server sometimes returns errors with a success status code for some reason, so catch that
        if (response.errorCode() != null) { // errorCode is the only required property in an error response
            throw response.createApiException();
        }

        return response.data();
    }

    /**
     * Sends a request to the server and reads the response as raw data.
     *
     * @param function     name of the API function to execute
     * @param functionData data for the function, or {@code null} to not include data in the request
     * @return input stream containing the raw data
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public InputStream requestRaw(String function, Object functionData) {
        return getRawResponse(createRequest(function, functionData));
    }

    /**
     * Sends a request to the server with no data for the function and reads the response as raw data.
     *
     * @param function name of the API function to execute
     * @return input stream containing the raw data
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public InputStream requestRaw(String function) {
        return requestRaw(function, null);
    }

    /**
     * Sends a request to the server.
     *
     * @param function         name of the API function to execute
     * @param functionData     data for the function, or {@code null} to not include data in the request
     * @param responseDataType class to parse the response data into
     * @param <R>              type to return
     * @return response data, or {@code null} if there was none
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public <R> R request(String function, Object functionData, Class<R> responseDataType) {
        return parseResponse(createRequest(function, functionData), responseDataType);
    }

    /**
     * Sends a request to the server with no data for the function.
     *
     * @param function         name of the API function to execute
     * @param responseDataType class to parse the response data into
     * @param <R>              type to return
     * @return response data, or {@code null} if there was none
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public <R> R request(String function, Class<R> responseDataType) {
        return request(function, null, responseDataType);
    }

    /**
     * Sends a request to the server expecting no response data.
     *
     * @param function     name of the API function to execute
     * @param functionData data for the function, or {@code null} to not include data in the request
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public void request(String function, Object functionData) {
        request(function, functionData, Void.class);
    }

    /**
     * Sends a request to the server with no data for the function and also expecting no response data.
     *
     * @param function name of the API function to execute
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public void request(String function) {
        request(function, (Object) null);
    }

    /**
     * Sends a multipart request to the server.
     *
     * @param function     name of the API function to execute
     * @param functionData data for the function, or {@code null} to not include data in the request
     * @param partName     name of the other part of the request
     * @param partData     input stream containing the raw data for the other part of the request
     * @param responseDataType class to parse the response data into
     * @param <R>              type to return
     * @return response data, or {@code null} if there was none
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public <R> R multipartRequest(String function, Object functionData, String partName, InputStream partData, Class<R> responseDataType) {
        return parseResponse(createMultipartRequest(function, functionData, partName, partData), responseDataType);
    }

    /**
     * Sends a multipart request to the server with no data for the function.
     *
     * @param function name of the API function to execute
     * @param partName name of the other part of the request
     * @param partData input stream containing the raw data for the other part of the request
     * @param responseDataType class to parse the response data into
     * @param <R>              type to return
     * @return response data, or {@code null} if there was none
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public <R> R multipartRequest(String function, String partName, InputStream partData, Class<R> responseDataType) {
        return multipartRequest(function, null, partName, partData, responseDataType);
    }

    /**
     * Sends a multipart request to the server expecting no response data.
     *
     * @param function     name of the API function to execute
     * @param functionData data for the function, or {@code null} to not include data in the request
     * @param partName     name of the other part of the request
     * @param partData     input stream containing the raw data for the other part of the request
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public void multipartRequest(String function, Object functionData, String partName, InputStream partData) {
        multipartRequest(function, functionData, partName, partData, Void.class);
    }

    /**
     * Sends a multipart request to the server with no data for the function and also expecting no response data.
     *
     * @param function name of the API function to execute
     * @param partName name of the other part of the request
     * @param partData input stream containing the raw data for the other part of the request
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public void multipartRequest(String function, String partName, InputStream partData) {
        multipartRequest(function, null, partName, partData);
    }

    /**
     * Sends a request to the server expecting a new authentication token in response.
     * <p>
     * Upon success, the current authentication token is updated for use by subsequent requests.
     *
     * @param function     name of the API function to execute
     * @param functionData data for the function, or {@code null} to not include data in the request
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public void requestToken(String function, Object functionData) {
        record Schema(String authenticationToken) {
        }
        String newToken = request(function, functionData, Schema.class).authenticationToken;
        if (newToken != null && !newToken.isEmpty()) {
            token = newToken;
        }
    }

    /**
     * Sends a request to the server with no data for the function and also expecting a new authentication token in response.
     * <p>
     * Upon success, the current authentication token is updated for use by subsequent requests.
     *
     * @param function name of the API function to execute
     * @throws ApiException     if an API error occurs
     * @throws RequestException if an error occurs while sending the request
     */
    public void requestToken(String function) {
        requestToken(function, null);
    }

}
