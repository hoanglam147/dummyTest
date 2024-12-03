package com.secutix.util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.XML;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RestAssureHelper {
    private static String proxyIp = "172.27.33.48";
    private static int port = 3128;
    public static JSONObject sendRequestAndGetResponse(String soapRequest, String postUri, String baseUri) {
        RestAssured.baseURI = baseUri;
        Response response = given().
                header("Content-Type", "text/xml").
                header("User-Agent", "SauceLabs/API-Fortress - WSTestJS").
                header("accept-encoding", "gzip, deflate").
                body(soapRequest).
                when().
                post(postUri).
                then().
                statusCode(200).extract().response();

        // Optionally, assert response body content
        // Note: This example assumes the response contains some expected text.
        String responseBody = response.getBody().asString();
        JSONObject jsonResponse = XML.toJSONObject(responseBody);
        return jsonResponse;

    }
    public static Response sendGetRequest(String URL) {
        return RestAssured
                .given()
                .header("User-Agent", "API-Java-Secutix")
                .when()
                .get(URL);
    }
    public static Response sendGetRequestWithProxy(String URL) {
        return RestAssured
                .given()
                .proxy(proxyIp, port)
                .header("User-Agent", "API-Java-Secutix")
                .when()
                .get(URL);
    }
}
