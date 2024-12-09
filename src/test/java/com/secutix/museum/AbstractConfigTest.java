package com.secutix.museum;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public abstract class AbstractConfigTest {

    // Predefined fields with default/empty values
    protected static String  apiUrl= "" ;
    protected static String  currentDay= "" ;
    protected static String  currentMonth= "" ;
    protected static String  currentYear= "" ;
    protected static String  host= "" ;
    protected static String  inOneWeek= "" ;
    protected static String  now= "" ;
    protected static String  today= "" ;
    protected static String  availabilityServiceUrl= "" ;
    protected static String  catalogServiceUrl= "" ;
    protected static String  contactAuthenticationPublicServiceUrl= "" ;
    protected static String  contactInformationPublicServiceUrl= "" ;
    protected static String  contactInteractionPublicServiceUrl= "" ;
    protected static String  contactLoginServiceUrl= "" ;
    protected static String  contactSocialLoginServiceUrl= "" ;
    protected static String  dataExportServiceUrl= "" ;
    protected static String  externalAccessControlServiceUrl= "" ;
    protected static String  externalCampaignServiceUrl= "" ;
    protected static String  externalOrderFacadeUrl= "" ;
    protected static String  externalOrderHistoryServiceUrl= "" ;
    protected static String  externalOrderServiceUrl= "" ;
    protected static String  externalPaymentServiceUrl= "" ;
    protected static String  password= "" ;
    protected static String  passwordAC= "" ;
    protected static String  productionPublicServiceUrl= "" ;
    protected static String  questionnaireServiceUrl= "" ;
    protected static String  setupServiceUrl= "" ;
    protected static String  shipmentPublicServiceUrl= "" ;
    protected static String  username= "" ;
    protected static String  usernameAC= "" ;
    protected static String  versionA= "" ;
    protected static String  versionB= "" ;
    protected static String  versionC= "" ;
    protected static String  versionD= "" ;
    protected static String  versionE= "" ;
    protected static String  versionF= "" ;
    protected static String posUrl = "";
    private static Map<String, String> variables = null;
    protected static ArrayList<String> specificUrls = new ArrayList<>();

    @BeforeAll
    public static void loadConfiguration() {
        System.out.println("Running @BeforeAll setup...");
        if(variables != null) return;
        String jsonFilePath = "C:\\Users\\hoal\\Downloads\\vault-P17 - Top 6 tests-1733466316298.json"; // Path to the JSON file
//        String jsonFilePath = System.getenv("CONFIG_FILE_PATH");
        System.out.println(jsonFilePath);
        try {
            // Parse the JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonData = objectMapper.readValue(new File(jsonFilePath), Map.class);

            // Extract "variables" section
            if (jsonData.containsKey("variables")) {
                Map<String, String> variabless = (Map<String, String>) jsonData.get("variables");
                variables = resolvePlaceholders(variabless);
                // Get all fields in the class and set values dynamically
                for (Field field : AbstractConfigTest.class.getDeclaredFields()) {
                    field.setAccessible(true); // Make private/protected fields accessible
                    String fieldName = field.getName();
                    if (variables.containsKey(fieldName)) {
                        field.set(null, variables.get(fieldName)); // Set the field value
                        System.out.println("Set " + fieldName + " to " + variables.get(fieldName));
                    }
                }
            }
            if (jsonData.containsKey("specificUrls")) {
                specificUrls = new ArrayList<>(Arrays.asList(jsonData.get("specificUrls").toString().
                        replaceAll("\\[", "").replaceAll("]", "").split(",")));
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load and assign configuration: " + e.getMessage(), e);
        }
    }

    public static Map<String, String> resolvePlaceholders(Map<String, String> variables) {
        Map<String, String> resolved = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String key = entry.getKey();
            String value = resolveValue(entry.getValue(), variables, resolved);
            resolved.put(key, value);
        }

        return resolved;
    }
    private static String resolveValue(String value, Map<String, String> allVariables, Map<String, String> resolvedVariables) {
        if (value == null) return null;

        // Check if the value contains placeholders
        while (value.contains("${")) {
            int start = value.indexOf("${");
            int end = value.indexOf("}", start);
            if (end == -1) break; // Malformed placeholder

            String placeholder = value.substring(start + 2, end);
            String resolvedValue = resolvedVariables.getOrDefault(placeholder, allVariables.get(placeholder));

            if (resolvedValue == null) {
                throw new IllegalArgumentException("Undefined variable: " + placeholder);
            }

            // Replace the placeholder with its value
            value = value.substring(0, start) + resolvedValue + value.substring(end + 1);
        }

        return value;
    }

}
