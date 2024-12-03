package com.secutix.data;

import java.util.List;

public enum UrlData implements UrlDataGrouppingList{
    NO_WEHRE ("nowhere", null),
    P_17_URLS("stxcat17", UrlGroups.getP17Urls());
    private final String place;
    private final List<String> urls;
    @Override
    public String getPlace() {
        return place;
    }

    public List<String> getUrls() {
        return urls;
    }
    @Override
    public List<String> getListUrls(String place) {
        List<String> listUrls = null;
        for (UrlData member : UrlData.values()) {
            if(member.getPlace().equals(place)) {
                listUrls = member.getUrls();
            }
        }
        return listUrls;
    }
    UrlData(String place, List<String>urls) {
        this.place = place;
        this.urls = urls;
    }
}
