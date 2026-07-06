package net.trollyloki.jicsit.modding;

import org.jspecify.annotations.NullMarked;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipInputStream;

/**
 * Client for the <a href="https://ficsit.app">Satisfactory Mod Repository</a> API.
 */
@NullMarked
public class SmrApi {

    private final URI baseUri;
    private final HttpClient httpClient;
    private final JsonMapper jsonMapper;

    private record VersionListResponseSchema(
            ModVersionArrayList data
    ) {
    }

    /**
     * Creates a new client for the <a href="https://ficsit.app">Satisfactory Mod Repository</a> API.
     */
    public SmrApi() {
        try {
            this.baseUri = new URI("https", "api.ficsit.app", "/v1/", null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
        this.jsonMapper = JsonMapper.builder().propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).build();
    }

    private <T> CompletableFuture<HttpResponse<T>> sendRequest(String path, HttpResponse.BodyHandler<T> bodyHandler) {
        HttpRequest request = HttpRequest.newBuilder(baseUri.resolve(path)).build();
        return httpClient.sendAsync(request, responseInfo -> {
            if (responseInfo.statusCode() != 200) {
                throw new RuntimeException(String.valueOf(responseInfo.statusCode()));
            }
            return bodyHandler.apply(responseInfo);
        });
    }

    /**
     * Gets all available versions of mod.
     *
     * @param modReference mod reference
     * @return {@link ModVersionList} future
     */
    public CompletableFuture<ModVersionList> getAllModVersions(String modReference) {
        String path = "mod/" + modReference + "/versions/all";

        return sendRequest(path, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(httpResponse -> {
            VersionListResponseSchema response = jsonMapper.readValue(httpResponse.body(), VersionListResponseSchema.class);
            return response.data;
        });
    }

    /**
     * Downloads the archive of a mod version for a given installation target.
     *
     * @param versionId SMR version ID
     * @param targetName installation target
     * @return {@link ZipInputStream} future
     */
    public CompletableFuture<ZipInputStream> downloadModVersion(String versionId, String targetName) {
        String path = "version/" + versionId + "/" + targetName + "/download";

        return sendRequest(path, HttpResponse.BodyHandlers.ofInputStream()).thenApplyAsync(httpResponse -> {
            InputStream stream = httpResponse.body();
            return new ZipInputStream(stream);
        });
    }

    /**
     * Downloads the archive of a mod version for a given installation target.
     *
     * @param modVersionTarget {@link ModVersionTarget}
     * @return {@link ZipInputStream} future
     */
    public CompletableFuture<ZipInputStream> downloadModVersion(ModVersionTarget modVersionTarget) {
        return downloadModVersion(modVersionTarget.versionId(), modVersionTarget.targetName());
    }

}
