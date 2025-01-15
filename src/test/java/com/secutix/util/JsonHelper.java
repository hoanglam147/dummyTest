package com.secutix.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.XML;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

public class JsonHelper {

    private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);

    private static JsonPath jsonPath;
    // Getter method to initialize JsonPath lazily

    private static JsonPath getJsonPath(Response response) {
        if (jsonPath == null) {
            jsonPath = JsonPath.from(XML.toJSONObject(response.getBody().asString()).toString());
        }
        return jsonPath;
    }
    public static String getValueJsonResponse(Response responseJson, String map, String key) {
        // Use JsonHelper.getValue to fetch the value at the expression
        Map<String, Object> object  = responseJson.jsonPath().getMap(map);

        // Assert that the value is not null

        return object.get(key).toString();
    }

    public static void assertResponseJson(Response response, String keyPath) {
        JsonPath jsonPath = JsonPath.from(response.asString());

        // Get the value using the provided key path
        Object value = jsonPath.get(keyPath);

        if (value == null) {
            throw new AssertionError("Value not found for key path: " + keyPath);
        }

    }

    public static void resetJsonPath() {
        jsonPath = null;
    }
    // Verify if a key exists in the JSON response
    public static void assertKeyExists(Response response, String map, String key) {
//        logger.info("verify map: " + map + " has key: " + key );
        JsonPath jsonPath = getJsonPath(response);
        assertTrue(jsonPath.getMap(map).containsKey(key), "Key '" + key + "' does not exist in the response: " + response.toString());
    }

    public static void assertResponseStringContains(Response response, String value) {
//        logger.info("verify response contains " + value );

        assertTrue(response.asString().contains(value), "Response '" + response.asString() + "' does not exist value: " +value);
    }

    // Verify if the value of a key matches the expected value
    public static String assertValueEquals(Response response, String key, String expectedValue) {
        JsonPath jsonPath = getJsonPath(response);
        String actualValue = jsonPath.getString(key);
        assertNotNull(actualValue, "Value for key '" + key + "' is null.");
        if(expectedValue != null) {
            assertEquals(expectedValue, actualValue,
                    "Expected value for key '" + key + "' is '" + expectedValue + "', but got '" + actualValue + "'.");
        }
        return actualValue;
    }

//    public static String assertValueEquals(Response response, String key, String... expectedValues) {
//        JsonPath jsonPath = getJsonPath(response);
//        String actualValue = jsonPath.getString(key);
//        assertNotNull(actualValue, "Value for key '" + key + "' is null. See response: " +response.toString());
//
//        boolean matchFound = false;
//
//        // Check if actualValue matches any of the expectedValues
//        if(expectedValues != null) {
////            logger.info("verify key : " + key + " has values amongs: " + String.join("", expectedValues) );
//            for (String expectedValue : expectedValues) {
//                if (expectedValue != null && expectedValue.equals(actualValue)) {
//                    matchFound = true;
//                    break;
//                }
//            }
//            if (!matchFound) {
//                assertEquals(expectedValues[0], actualValue,
//                        "Expected value for key '" + key + "' is one of " + Arrays.toString(expectedValues)
//                                + ", but got '" + actualValue + "'.");
//            }
//        }
//
//        // If no match is found, assert failure
//        return actualValue;
//    }


    // Verify if the value of a key is not null or empty
    public static void assertNotEmpty(Response response, String key) {
        JsonPath jsonPath = getJsonPath(response);
        String actualValue = jsonPath.getString(key);
        assertNotNull(actualValue, "Value for key '" + key + "' is null. Please check response: " + response.toString());
        assertFalse(actualValue.isEmpty(), "Value for key '" + key + "' is empty. Please check reposne: " + response);
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
//        logger.info("check response is: " + expectedStatusCode);
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            throw new AssertionError("Expected status code: " + expectedStatusCode + ", but got: " + actualStatusCode);
        }
    }
}

//package com.secutix.util;
//
//import io.restassured.response.Response;
//import org.junit.jupiter.api.Assertions;
//import org.w3c.dom.Document;
//
//import java.util.logging.Logger;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//public class AssertionHelper {
//    private static final Logger logger = Logger.getLogger(AssertionHelper.class.getName());
//    public static String assertAndLog(Response response, String fieldName, String value) {
//        String returnOnDemand = null;
//        try {
//            logger.info("assert " + fieldName + " exists");
//            returnOnDemand = response.jsonPath().getString(fieldName);
//            Assertions.assertNotNull(returnOnDemand, "Expected " + fieldName + " to be present, but it was null.");
//            if(value != null) {
//                Assertions.assertTrue(returnOnDemand.equalsIgnoreCase(value), String.format("Expected is: %s, Actual is: %s",value, response.jsonPath().getString(fieldName)));
//                logger.info("assert " + fieldName + " had value " + value);
//            }
//
//        } catch (AssertionError e) {
//            logger.severe("Assertion NOT OK: " + e.getMessage());
//            throw e; // Re-throw to fail the test
//        }
//        return returnOnDemand;
//    }
//    public static void assertAndLog(Document doc, String nodes, String values) {
//        logger.info("assert " + nodes + " exists");
//        Assertions.assertTrue(doc.getElementsByTagName(nodes).getLength() > 0, "Expected " + nodes + " to be present, but it was null.");
//        if(values != null) {
//            Assertions.assertEquals(values, doc.getElementsByTagName(nodes).item(0).getTextContent(),
//                    String.format("Expected is: %s, Actual is: %s",values, doc.getElementsByTagName(nodes).item(0).getTextContent()));
//
//            logger.info("assert " + nodes + " had value " + values); }
//    }
//    public static void assertAndLogResponseCode(Response response) {
//        logger.info("Verify response is 200");
//        assertEquals(200, response.getStatusCode(),
//                String.format("Expected 200, but actual %s", response.getStatusCode()));
//    }
//    public static void assertAndLogHeader(Response response, String headerName, String values) {
//        logger.info(String.format("Verify header contains %s", values));
//        assertEquals(values, response.getHeader(headerName),
//                String.format("Expected %s, but actual %s", values, response.getHeader(headerName)));
//    }
//}
