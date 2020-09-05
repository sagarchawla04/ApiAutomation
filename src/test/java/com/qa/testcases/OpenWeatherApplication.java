package com.qa.testcases;

import com.qa.apiApps.OpenWeatherAppPage;
import com.qa.utils.apiUtils;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class OpenWeatherApplication extends apiUtils {
    OpenWeatherAppPage openWeatherAppPage = new OpenWeatherAppPage();
    public String key;

    @Test
    public void RegisterStationWithoutKey() throws IOException, ParseException {
        endPointURL = "http://api.openweathermap.org";
        pathParam = "/data/3.0/stations";
        // Build the request
        headerMap.put("Content-Type", "application/json");
        builder = buildRequestWithEndpointNHeader(endPointURL, headerMap);
        builder.setBasePath(pathParam);
        builder.setBody(openWeatherAppPage.createBodyForRegisteringStation());
        //addCertificateToRequest();
        requestSpecification = builder.build();
        // Post API Call
        response = postCall(requestSpecification, false);
        // Assert response code
        expectedStatusCode = 401;
        validateResponseCode(response, 401);
        // Validate message from response
        Assert.assertEquals(getValueFromResponse(response, "message"), "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info.", "The message is not as expected");
    }

    @Test
    public void RegisterStationWithKey() throws IOException, ParseException {
        endPointURL = "http://api.openweathermap.org";
        pathParam = "/data/3.0/stations";
        headerMap.put("Content-Type", "application/json");
        queryParamMap.put("APPID", "fe22c83dfccfeafa0301fb8eb3e293ae");

        // Build the request
        builder = buildRequestWithEndpointNHeader(endPointURL, headerMap);
        builder.setBasePath(pathParam);
        builder.setBody(openWeatherAppPage.createBodyForRegisteringStation());
        builder.addQueryParams(queryParamMap);


        //addCertificateToRequest();
        requestSpecification = builder.build();
        // Post API Call
        response = postCall(requestSpecification, false);
        // Assert response code
        expectedStatusCode = 201;
        validateResponseCode(response, 201);
        // Validate message from response
        //Assert.assertEquals(getValueFromResponse(response, "message"), "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info.", "The message is not as expected");
    }


}
