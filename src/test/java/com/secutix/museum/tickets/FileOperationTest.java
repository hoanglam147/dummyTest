package com.secutix.museum.tickets;
import com.secutix.museum.AbstractConfigTest;
import com.secutix.util.*;
//import io.qameta.allure.Step;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileOperationTest extends AbstractConfigTest {
    private static final Logger logger = LoggerFactory.getLogger(FileOperationTest.class);

    @Test
    @Timeout(value = 5, unit = TimeUnit.MINUTES)
    @Step("Step n")
    public void testResponseAssertions() throws InterruptedException {
        String pointOfSalesId = "466653735";
        String contactNumber = "7697";

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "API ForTress");
        headers.put("Content-Type", "text/xml");
        String bodySoapEnv =  String.format(
                "<v1:createOrUpdateOrder>" +
                        "<pointOfSalesId>%s</pointOfSalesId>" +
                        "<orderType>SALE</orderType>" +
                        "<purchasersInfo>" +
                        "<contactReference>%s</contactReference>" +
                        "</purchasersInfo>" +
                        "<singleEntryAttributionOperations>" +
                        "<operationAttributions>" +
                        "<audienceSubCategoryId>306159078</audienceSubCategoryId>" +
                        "<quantity>1</quantity>" +
                        "<performanceId>101036924388</performanceId>" +
                        "<seatCategoryId>466681816</seatCategoryId>" +
                        "</operationAttributions>" +
                        "<preferedAreas>466636464</preferedAreas>" +
                        "</singleEntryAttributionOperations>" +
                        "</v1:createOrUpdateOrder>",
                pointOfSalesId, contactNumber
        );



        String body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderFacade", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();
        Response response = RestAssureHelper.sendRequest(body, externalOrderFacadeUrl, headers, "POST");
        JsonHelper.assertStatusCode(response, 200);
        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:createOrUpdateOrderResponse'.OrderUpdateResult.statusCode", "success");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:createOrUpdateOrderResponse'.OrderUpdateResult", "expectedException");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:createOrUpdateOrderResponse'.OrderUpdateResult.orderUpdateData", "movementDataUpdates");
        String movementId = JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:createOrUpdateOrderResponse'.OrderUpdateResult.orderUpdateData.movementDataUpdates.movementId", null);
        String fileId = JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:createOrUpdateOrderResponse'.OrderUpdateResult.orderUpdateData.operationDataUpdates.fileId", null);
        String operationId = JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:createOrUpdateOrderResponse'.OrderUpdateResult.orderUpdateData.operationDataUpdates.operationId", null);
        String orderId = JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:createOrUpdateOrderResponse'.OrderUpdateResult.orderUpdateData.orderId", null);
        String orderSecretId = JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:createOrUpdateOrderResponse'.OrderUpdateResult.orderUpdateData.orderSecretId", null);

        bodySoapEnv =  String.format("<v1:getFileDetails><fileId>%s</fileId></v1:getFileDetails>",fileId);

        body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderService", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();
        response = RestAssureHelper.sendRequest(body, externalOrderServiceUrl, headers, "POST");
        JsonHelper.assertStatusCode(response, 200);

        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsResponse'.FileDetailResult.statusCode", "success");
// HOAL --> need verify logic NOT        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsResponse'.FileDetailResult.fileDetailsData", "fileAttachments");


        bodySoapEnv =  String.format(
                "<v1:uploadFileAttachment>" +
                        "<fileId>%s</fileId>" +
                        "<printableFileAttachmentData>" +
                        "<data>%s</data>" +
                        "<fileName>%s</fileName>" +
                        "<mimetype>%s</mimetype>" +
                        "<uploadDate>%s</uploadDate>" +
                        "</printableFileAttachmentData>" +
                        "<contactReference>%s</contactReference>" +
                        "</v1:uploadFileAttachment>",
                fileId, "cid:782441202438", "New file", "application/pdf", "08/07/2020", contactNumber
        );

        body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderService", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();


        response = RestAssureHelper.sendRequest(body, externalOrderServiceUrl, headers, "POST");
        JsonHelper.assertStatusCode(response, 200);

        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:uploadFileAttachmentResponse'.WebMethodResult.statusCode", "success");


        bodySoapEnv =  String.format(
                "<v1:getFileDetailsFromOrderSecretId><orderSecretId>%s</orderSecretId></v1:getFileDetailsFromOrderSecretId>", orderSecretId);

        body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderService", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();
        response = RestAssureHelper.sendRequest(body, externalOrderServiceUrl, headers, "POST");
        JsonHelper.assertStatusCode(response, 200);

        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.statusCode", "success");

        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.fileDetailsData.fileAttachments", "fileAttachmentId");
        String fileAttachmentId = JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.fileDetailsData.fileAttachments.fileAttachmentId", null);

        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.fileDetailsData.fileAttachments", "name");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.fileDetailsData.fileAttachments", "uploadDate");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.fileDetailsData", "fileContacts");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.fileDetailsData", "fileId");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.fileDetailsData", "fileState");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.fileDetailsData", "lastModDateTime");

        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "catalogCurrency");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "contactReference");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "creationDateTime");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "expirationDate");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "movementDatas");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "operationDatas");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "optionAmount");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "orderId");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "orderOriginData");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "orderOriginator");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "orderSecretId");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "orderState");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "orderType");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "preSaleAmount");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "referenceDate");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "reservationAmount");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "saleAmount");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "salesChannelCode");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "salesChannelName");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileDetailsFromOrderSecretIdResponse'.FileDetailResult.orderDetailsDatas", "waitingAccountBalanceAmount");

        bodySoapEnv =  String.format(
                "<v1:getFileAttachment>" +
                        "<fileAttachmentId>%s</fileAttachmentId>" +
                        "<contactReference>%s</contactReference>" +
                        "</v1:getFileAttachment>",
                fileAttachmentId, contactNumber
        );


        body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderService", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();
        response = RestAssureHelper.sendRequest(body, externalOrderServiceUrl, headers, "POST");
        JsonHelper.assertStatusCode(response, 200);
        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:getFileAttachmentResponse'.PrintableFileAttachmentResult.statusCode", "success");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileAttachmentResponse'.PrintableFileAttachmentResult.fileAttachment", "data");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileAttachmentResponse'.PrintableFileAttachmentResult.fileAttachment", "fileName");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileAttachmentResponse'.PrintableFileAttachmentResult.fileAttachment", "mimetype");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getFileAttachmentResponse'.PrintableFileAttachmentResult.fileAttachment", "uploadDate");


        bodySoapEnv =  String.format(
                "<v1:deleteFileAttachment>" +
                        "<fileAttachmentId>%s</fileAttachmentId>" +
                        "<contactReference>%s</contactReference>" +
                        "</v1:deleteFileAttachment>",
                fileAttachmentId, contactNumber
        );



        body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderService", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();

        response = RestAssureHelper.sendRequest(body, externalOrderServiceUrl, headers, "POST");
        JsonHelper.assertStatusCode(response, 200);
        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:deleteFileAttachmentResponse'.WebMethodResult.statusCode", "success");


        bodySoapEnv =  String.format(
                "<v1:updateExternalReferences>" +
                        "<id>%s</id>" +
                        "<externalReferenceKind>ORDER</externalReferenceKind>" +
                        "<externalReference>" +
                        "<key>%s</key>" +
                        "<value>%s</value>" +
                        "</externalReference>" +
                        "</v1:updateExternalReferences>",
                orderId, "TestKey", "TestValue"
        );
        body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderService", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();
        response = RestAssureHelper.sendRequest(body, externalOrderServiceUrl, headers, "POST");
        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:updateExternalReferencesResponse'.WebMethodResult.statusCode", "success");

        bodySoapEnv =  String.format(
                "<v1:getExternalReference><id>%s</id><externalReferenceKind>ORDER</externalReferenceKind></v1:getExternalReference>",
                orderId
        );
        body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderService", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();

        response = RestAssureHelper.sendRequest(body, externalOrderServiceUrl, headers, "POST");
        JsonHelper.assertStatusCode(response, 200);
        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:getExternalReferenceResponse'.ExternalReferenceResult.statusCode", "success");

        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getExternalReferenceResponse'.ExternalReferenceResult", "requestId");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getExternalReferenceResponse'.ExternalReferenceResult", "statusDetail");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:getExternalReferenceResponse'.ExternalReferenceResult.externalReferences", "externalReferences");
        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:getExternalReferenceResponse'.ExternalReferenceResult.externalReferences.externalReferences.key", "TestKey");
        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:getExternalReferenceResponse'.ExternalReferenceResult.externalReferences.externalReferences.value", "TestValue");

        orderId = JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:getExternalReferenceResponse'.ExternalReferenceResult.externalReferences.id", null);

        bodySoapEnv = String.format(
                "<v1:updateFileStatus>" +
                        "<fileId>%s</fileId>" +
                        "<fileStatusIC></fileStatusIC>" +
                        "</v1:updateFileStatus>",
                fileId
        );

        body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderService", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();

        response = RestAssureHelper.sendRequest(body, externalOrderServiceUrl, headers, "POST");
        JsonHelper.assertStatusCode(response, 200);
        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:updateFileStatusResponse'.WebMethodResult.statusCode", "success");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:updateFileStatusResponse'.WebMethodResult", "requestId");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:updateFileStatusResponse'.WebMethodResult", "statusDetail");

        bodySoapEnv = String.format(
                "<v1:cancelOperations>" +
                        "<orderId>%s</orderId>" +
                        "<operationIds>%s</operationIds>" +
                        "<movementIds>%s</movementIds>" +
                        "</v1:cancelOperations>",
                orderId, operationId, movementId
        );


        body = SoapRequestBuilder.buildSoapEnvelope(versionA, "ExternalOrderService", username, password, bodySoapEnv);
        JsonHelper.resetJsonPath();

        response = RestAssureHelper.sendRequest(body, externalOrderServiceUrl, headers, "POST");
        JsonHelper.assertStatusCode(response, 200);
        JsonHelper.assertValueEquals(response, "'S:Envelope'.'S:Body'.'ns2:cancelOperationsResponse'.OrderUpdateResult.statusCode", "success");

        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:cancelOperationsResponse'.OrderUpdateResult", "expectedException");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:cancelOperationsResponse'.OrderUpdateResult.orderUpdateData.catalogCurrency", "currencyCode");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:cancelOperationsResponse'.OrderUpdateResult.orderUpdateData.catalogCurrency", "fractionDigit");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:cancelOperationsResponse'.OrderUpdateResult.orderUpdateData.catalogCurrency", "minAmount");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:cancelOperationsResponse'.OrderUpdateResult.orderUpdateData.catalogCurrency", "symbol");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:cancelOperationsResponse'.OrderUpdateResult.orderUpdateData", "generatedContactQualities");
        JsonHelper.assertKeyExists(response, "'S:Envelope'.'S:Body'.'ns2:cancelOperationsResponse'.OrderUpdateResult.orderUpdateData", "orderId");


    }
}
