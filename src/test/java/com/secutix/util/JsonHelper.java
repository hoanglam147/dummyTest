package com.secutix.util;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.InputStream;

public class JsonHelper {


    /**
     * Reads a JSON object from a file.
     *
     * @param filePath the path to the JSON file
     * @return the JSON object
     * @throws Exception if there is an error reading the file
     */
    public static JSONObject readJsonFromFile(String filePath) throws Exception {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONObject(tokener);
        }
    }

    /**
     * Checks if a JSON object contains a specific key.
     *
     * @param json the JSON object
     * @param key the key to search for
     * @return true if the key is found, false otherwise
     */
    public static boolean containsKey(JSONObject json, String key) {
        return json.has(key);
    }

    public static void verifyTreeNodesExists(JSONObject json, String finalValue, String ... nodes) {
        JSONObject t_json = json;
        for(int i = 0; i < nodes.length; i++) {
            if(i == nodes.length-1 && finalValue != null) {
                assert containsValue(t_json, finalValue, nodes[i]);
            }
            else if(i == nodes.length-1 && finalValue == null) {
                assert containsKey(t_json, nodes[i]);
            }
            else {
                assert containsKey(t_json, nodes[i]);
                t_json = t_json.getJSONObject(nodes[i]);
            }

        }
    }
    public static String getValueOfKeyInJsonNode(JSONObject json, String key, String... nodes) {
        JSONObject t_json = json;
        String temp = "";
        for(int i = 0; i < nodes.length; i++) {
            t_json = t_json.getJSONObject(nodes[i]);
            if(i == nodes.length-1) {
                temp = getValue(t_json, key);
            }

        }
        return temp;
    }

    /**
     * Checks if a JSON object or its nested objects contain a specific value.
     *
     * @param json the JSON object
     * @param value the value to search for
     * @return true if the value is found, false otherwise
     */
    public static boolean containsValue(JSONObject json, Object value) {
        for (String key : json.keySet()) {
            Object val = json.get(key);
            if (val.equals(value)) {
                return true;
            }
            if (val instanceof JSONObject && containsValue((JSONObject) val, value)) {
                return true;
            }
            if (val instanceof JSONArray && containsValue((JSONArray) val, value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsValue(JSONObject json, Object value, String key) {
        Object val = json.get(key);
        if (val.equals(value)) {
            return true;
        }
        return  false;
    }
    public static String getValue(JSONObject json, String key) {
        Object val = json.get(key);
        return val.toString();
    }
    /**
     * Checks if a JSON array or its nested objects contain a specific value.
     *
     * @param array the JSON array
     * @param value the value to search for
     * @return true if the value is found, false otherwise
     */
    public static boolean containsValue(JSONArray array, Object value) {
        for (int i = 0; i < array.length(); i++) {
            Object val = array.get(i);
            if (val.equals(value)) {
                return true;
            }
            if (val instanceof JSONObject && containsValue((JSONObject) val, value)) {
                return true;
            }
            if (val instanceof JSONArray && containsValue((JSONArray) val, value)) {
                return true;
            }
        }
        return false;
    }

    public static JSONObject getChildObject(JSONObject json, String key) throws Exception {
        if (json.has(key)) {
            Object child = json.get(key);
            if (child instanceof JSONObject) {
                return (JSONObject) child;
            } else {
                throw new Exception("The key does not correspond to a JSON object.");
            }
        } else {
            throw new Exception("The key does not exist in the JSON object.");
        }
    }


}
