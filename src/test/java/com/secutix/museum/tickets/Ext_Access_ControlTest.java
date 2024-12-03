//package com.secutix.museum.tickets;
//
//import com.secutix.data.UrlData;
//import com.secutix.rule.RetryExtension;
//import com.secutix.util.Configuration;
//import com.secutix.util.RestAssureHelper;
//import io.qameta.allure.junit5.AllureJunit5;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import java.util.List;
//import static org.hamcrest.Matchers.*;
////@ExtendWith(RetryExtension.class)
////@ExtendWith(AllureJunit5.class)
//public class Ext_Access_ControlTest {
//    @Test
//    public void testEndpoint() {
//        String postUri = "/tnci/external-remoting/com.secutix.facade.control.v1_5.ExternalAccessControlService.webservice?wsdl";
//        for(String url : getUrls()) {
//            url = url + postUri;
//            try {
//                Response response = RestAssureHelper.sendGetRequest(url);
//                response.then().statusCode(anyOf(equalTo(200), equalTo(509)));
//            } catch (Exception e) {
//                Response response = RestAssureHelper.sendGetRequestWithProxy(url);
//                response.then().statusCode(anyOf(equalTo(200), equalTo(509)));
//            }
//        }
//    }
//    private List<String> getUrls() {
//        String institution = "stxcat" + Configuration.getSlotLazy();
//        String env = Configuration.getEnvLazy();
//        String finalVar = institution + env;
//        return UrlData.NO_WEHRE.getListUrls(finalVar);
//    }
//
//}
