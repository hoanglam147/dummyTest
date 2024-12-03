package com.secutix.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UrlGroups {
    public static final List<String> P_17_URLS = new ArrayList<>(Arrays.asList(
            "u1"
    ));
    public static final List<String> P_23_URLS = new ArrayList<>(Arrays.asList(
            "u1",
            "u2"));
    public static List<String> getP17Urls() {
        return P_17_URLS;
    }
    public static List<String> getP23Urls() {
        return P_23_URLS;
    }
}
