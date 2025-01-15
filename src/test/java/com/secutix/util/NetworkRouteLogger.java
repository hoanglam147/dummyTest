package com.secutix.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class NetworkRouteLogger {
    private static final Logger LOGGER = Logger.getLogger(NetworkRouteLogger.class.getName());
    public static void logRoute(String targetUrl) {
        String command = isWindows() ? "tracert /w 10000" : "/usr/bin/traceroute -w 5";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command, targetUrl);
            processBuilder.redirectErrorStream(true); // Merge stdout and stderr
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                LOGGER.info("Network route to: " + targetUrl);
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // Log each hop
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Traceroute command failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("Error while logging route: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

}
