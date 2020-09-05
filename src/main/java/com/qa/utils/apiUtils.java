package com.qa.utils;


import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class apiUtils {
    public RequestSpecBuilder builder;
    public RequestSpecification requestSpecification;
    public HashMap<String, String> headerMap = new HashMap<>();
    public Response response = null;
    public String endPointURL;
    public String pathParam;
    public int expectedStatusCode;
    public HashMap<String, String> queryParamMap = new HashMap<>();

    public String jksFilePath;
    public String jksFilePassword;
    public String jsonPathForProxy;

    /* Method to fectch the value from json file
     *
     */
    public String readJsonData(String jsonPath, String jsonField) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader(jsonPath);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        String value = (String) jsonObject.get(jsonField);
        return value;
    }


    /* Validate response code from API call
     *
     */
    public void validateResponseCode(Response response, int expectedStatusCode) {
        String responseBody = response.getBody().asString();
        System.out.println("Response body is " + responseBody);
        System.out.println("Response code is " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "The response code received is not as expected");
    }


    /* Validate response body value from API call
     *
     */
    public String getValueFromResponse(Response response, String responseFieldValue) {
        return response.jsonPath().get(responseFieldValue);
    }


    /* Read entire json - Usually used in case of static JSON's
     *
     */
    public String generateStringFromResource(String jsonPath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(jsonPath)));
    }


    /* Add certificate to Request - Specially for Salesforce certificate requests
     *
     */
    public void addCertificateToRequest(String jsonPath, String orgFilePath, String orgFilePassword) throws IOException, ParseException {
        jksFilePath = readJsonData(jsonPath, orgFilePath);
        jksFilePassword = readJsonData(jsonPath, orgFilePassword);

        System.setProperty("javax.net.ssl.trustStoreType", "jks");
        System.setProperty("javax.net.ssl.trustStore", jksFilePath);
        System.setProperty("javax.net.ssl.keyStorePassword", jksFilePassword);
        setPreemptiveProxy();
    }


    /* Set proxy for sending request
     *
     */
    public void setPreemptiveProxy() throws IOException, ParseException {
        jsonPathForProxy = "PathtoJson";
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName("username");
        authScheme.setPassword("password");
    }


    /* Build the request with end point and header
     *
     */
    public RequestSpecBuilder buildRequestWithEndpointNHeader(String endPoint, HashMap<String, String> headerMap) throws IOException, ParseException {
        return new RequestSpecBuilder().addHeaders(headerMap).setBaseUri(endPoint);
    }


    /* POST API Call with/without certificate
     *
     */
    public Response postCall(RequestSpecification requestSpecification, Boolean certificate) throws IOException, ParseException {
        if (certificate)
            return RestAssured.given().keyStore(jksFilePath, jksFilePassword).trustStore(jksFilePath, jksFilePassword).spec(requestSpecification).when().log().all().post();
        else
            return RestAssured.given().spec(requestSpecification).when().log().all().post();
    }


    /* GET API Call with/without certificate
     *
     */
    public Response getCall(RequestSpecification requestSpecification, Boolean certificate) throws IOException, ParseException {
        if (certificate)
            return RestAssured.given().keyStore(jksFilePath, jksFilePassword).trustStore(jksFilePath, jksFilePassword).spec(requestSpecification).when().log().all().get();
        else
            return RestAssured.given().spec(requestSpecification).when().log().all().get();
    }


    /* DELETE API Call with/without certificate
     *
     */
    public Response deleteCall(RequestSpecification requestSpecification, Boolean certificate) throws IOException, ParseException {
        if (certificate)
            return RestAssured.given().keyStore(jksFilePath, jksFilePassword).trustStore(jksFilePath, jksFilePassword).spec(requestSpecification).when().log().all().delete();
        else
            return RestAssured.given().spec(requestSpecification).when().log().all().delete();
    }


}
