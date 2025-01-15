package com.secutix.util;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;
public class RestAssureHelper {
    private static final Logger LOGGER = Logger.getLogger(RestAssureHelper.class.getName());
    private static RequestSpecification getRequestWithHeaders(Map<String, String> headers) {
        RequestSpecification request = RestAssured.given();
        // Add all headers from the map to the request
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.header(header.getKey(), header.getValue());
        }
        return request;
    }
    // Send GET or POST request based on input
    public static Response sendRequest(String body, String requestUri, Map<String, String> headers, String method) {
        RequestSpecification request = getRequestWithHeaders(headers);

        LOGGER.info(() -> "Sending " + method + " request to " + requestUri + " with headers: " + headers);

        // Constructing the curl command for logging purposes
        StringBuilder curlCommand = new StringBuilder("curl -X ")
                .append(method.toUpperCase())
                .append(" '").append(requestUri).append("'");

        if (headers != null && !headers.isEmpty()) {
            headers.forEach((key, value) -> curlCommand.append(" -H '").append(key).append(": ").append(value).append("'"));
        }

        if (body != null && !body.isEmpty()) {
            curlCommand.append(" --data '").append(body.replace("'", "\\'")).append("'");
        }

        LOGGER.info(() -> "Constructed curl command: " + curlCommand);

        Response response;
        try {
            if ("POST".equalsIgnoreCase(method)) {
                request.body(body);
                response = request.post(requestUri);
            } else if ("GET".equalsIgnoreCase(method)) {
                response = request.get(requestUri);
            } else {
                throw new IllegalArgumentException("Unsupported method: " + method);
            }
        } catch (Exception e) {
            LOGGER.info(() -> "Request failed with curl: " + curlCommand.toString());
            try {
                DebugUtils.pingServer(new URL(requestUri).getHost());
                DebugUtils.isPortOpen(new URL(requestUri).getHost(), 443);
                NetworkRouteLogger.logRoute(new URL(requestUri).getHost());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }

            throw e;
        }
        LOGGER.info(() -> "Received response: " + response.getStatusCode());
        return response;
    }

    public static Response sendRequestWithCookies(String body, String requestUri, Map<String, String> headers,  Map<String, String> cookies, String method) {
        RequestSpecification request = getRequestWithHeaders(headers);
        // Add cookies to the request
        if (cookies != null) {
            request.cookies(cookies);
        }

        if (body != null) {
            request.body(body);
        }

        Response response;
        if ("POST".equalsIgnoreCase(method)) {
            response = request.post(requestUri);
        } else if ("GET".equalsIgnoreCase(method)) {
            response = request.get(requestUri);
        } else {
            throw new IllegalArgumentException("Unsupported method: " + method);
        }

        return response;
    }

    // Validate response status and body
    public static void validateResponse(Response response, int expectedStatusCode, String expectedContent) {
        int actualStatusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();

        LOGGER.info(() -> "Validating response with status code: " + actualStatusCode);

        if (actualStatusCode != expectedStatusCode) {
            LOGGER.severe(() -> "Expected status: " + expectedStatusCode + ", but got: " + actualStatusCode);
            throw new AssertionError("Unexpected status code!");
        }

        if (!responseBody.contains(expectedContent)) {
            LOGGER.severe(() -> "Response body does not contain expected content: " + expectedContent);
            throw new AssertionError("Unexpected response content!");
        }

        LOGGER.info(() -> "Response validation passed.");
    }
}
