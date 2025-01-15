package com.secutix.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Logger;

public class DebugUtils {
    private static final Logger LOGGER = Logger.getLogger(DebugUtils.class.getName());
    // Ping an IP or hostname to check if it's reachable
    public static boolean pingServer(String host) {
        try {
            InetAddress inet = InetAddress.getByName(host);
            LOGGER.info("Pinging " + host + "...");
            boolean isReachable = inet.isReachable(5000); // Timeout: 5 seconds
            LOGGER.info("Ping result: " + (isReachable ? "Reachable" : "Unreachable"));
            return isReachable;
        } catch (Exception e) {
            LOGGER.info("Ping failed: " + e.getMessage());
            return false;
        }
    }

    // Check if a specific port is open on a host
    public static boolean isPortOpen(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            LOGGER.info("Port " + port + " on " + host + " is open.");
            return true;
        } catch (Exception e) {
            LOGGER.info("Port " + port + " check failed: " + e.getMessage());
            return false;
        }
    }

    // Send an HTTP request and log the response
    public static void checkAPI(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5 seconds
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            LOGGER.info("HTTP Response Code: " + responseCode);

            // Read the response if successful
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    LOGGER.info(line);
                }
                reader.close();
            } else {
                LOGGER.info("HTTP error: " + connection.getResponseMessage());
            }

        } catch (Exception e) {
            LOGGER.info("API check failed: " + e.getMessage());
        }
    }

    // Run a system command (e.g., curl, telnet)
    public static void runCommand(String command) {
        try {
            LOGGER.info("Executing command: " + command);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                LOGGER.info(line);
            }

            reader.close();
            process.waitFor();
            LOGGER.info("Command execution completed.");
        } catch (Exception e) {
            LOGGER.info("Command execution failed: " + e.getMessage());
        }
    }

    // Main function for testing
    public static void main(String[] args) {
        String host = "example.com"; // Replace with your host
        int port = 443; // Replace with your port
        String apiUrl = "https://example.com/api"; // Replace with your API URL

        // Step 1: Ping the server
        pingServer(host);

        // Step 2: Check if the port is open
        isPortOpen(host, port);

        // Step 3: Check the API
        checkAPI(apiUrl);

        // Step 4: Run custom commands (e.g., curl)
        runCommand("curl -I " + apiUrl);
    }
}
