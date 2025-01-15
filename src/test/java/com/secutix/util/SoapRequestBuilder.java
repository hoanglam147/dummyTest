package com.secutix.util;

public class SoapRequestBuilder {
    public static String buildSoapEnvelope(String version, String serviceName, String username, String password, String bodyContent) {
        return String.format("<soapenv:Envelope\n" +
                             "                xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                             "                xmlns:v1=\"http://v%s.%s.service.secutix.com\"\n" +
                             "                xmlns:ns2=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
                             "    <soapenv:Header>\n" +
                             "        <ns2:Security>\n" +
                             "            <ns2:UsernameToken>\n" +
                             "                <ns2:Username>%s</ns2:Username>\n" +
                             "                <ns2:Password>%s</ns2:Password>\n" +
                             "            </ns2:UsernameToken>\n" +
                             "        </ns2:Security>\n" +
                             "    </soapenv:Header>\n" +
                             "    <soapenv:Body>\n" +
                             "        %s\n" +
                             "    </soapenv:Body>\n" +
                             "</soapenv:Envelope>", version, serviceName, username, password, bodyContent);
    }
    public static String buildSoapEnvelopeToken(String version, String serviceName, String username, String password, String bodyContent) {
        return String.format("<soapenv:Envelope\n" +
                             "\txmlns:v1=\"http://v%s.%s.service.secutix.com/\"\n" +
                             "\txmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                             "\t<soapenv:Header>\n" +
                             "\t\t<wsse:Security\n" +
                             "\t\t\txmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
                             "\t\t\t<wsse:UsernameToken\n" +
                             "\t\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"UsernameToken-1\">\n" +
                             "\t\t\t\t<wsse:Username>%s</wsse:Username>\n" +
                             "\t\t\t\t<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">%s</wsse:Password>\n" +
                             "\t\t\t</wsse:UsernameToken>\n" +
                             "\t\t</wsse:Security>\n" +
                             "\t</soapenv:Header>\n" +
                             "\t<soapenv:Body>\n" +
                             "        %s\n" +
                             "\t</soapenv:Body>\n" +
                             "</soapenv:Envelope>", version, serviceName, username, password, bodyContent);
    }

}
