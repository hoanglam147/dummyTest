package com.secutix.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public final class Configuration {
    private final Map<String, String> wholeEnv;
    private static String institution;
    private static String env;
    private Configuration() {
        super();
        String ENV = "";
        String INSTITUTION = "17";

        wholeEnv = System.getenv();
        env = getPropertyOr("shop_env", ENV);
        institution = getPropertyOr("institution", INSTITUTION);
    }
    private String getPropertyOr(final String propertyName, final String defaultValue) {
        assert propertyName != null;
        assert defaultValue != null;

        final String value = wholeEnv.get(propertyName);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }
    private String getSlot() {
        return institution;
    }
    private String getEnv(){
        return env;
    }
    public static String getEnvLazy(){
        if(getInstance().getEnv().isEmpty() || getInstance().getEnv().equals("P")) {
            return "";
        } else {
            return getInstance().getEnv();
        }
    }
    public static String getSlotLazy(){
        return getInstance().getSlot();
    }
    private static class SingletonHolder {
        private static final Configuration INSTANCE = new Configuration();

        static Configuration getInstance() {
            return INSTANCE;
        }
    }
    private static Configuration getInstance() {
        return SingletonHolder.getInstance(); // lazy initialized
    }
}
