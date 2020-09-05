package com.qa.apiApps;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class OpenWeatherAppPage  {
    Map<String, Object> requestBody = new HashMap<>();
    public static String externalId;
    public static String stationName;

    public Map<String,Object> createBodyForRegisteringStation(){
        externalId = "SF_TEST"+ RandomStringUtils.randomNumeric(5);
        stationName = "Interview Station "+ RandomStringUtils.randomNumeric(5);
        requestBody.put("external_id","DEMO_TEST001");
        requestBody.put("name",stationName);
        requestBody.put("latitude",33.33);
        requestBody.put("longitude",-111.43);
        requestBody.put("altitude",444);
        return  requestBody;
    }
}
