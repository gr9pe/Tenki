package com.tenki.tenki;
import java.beans.ConstructorProperties;
import java.io.Serializable;

import com.fasterxml.jackson.databind.JsonNode;

public class Tenki implements Serializable{
    String locate;
    String weather;
    Double temp;
    Double wind;
    Double clouds; 

    public Tenki(){
        super();
    }

    @ConstructorProperties({"json"})
    public Tenki(JsonNode j){
        this.locate = j.get("name").textValue();
        this.weather = j.get("weather").get(0).get("main").asText();
        this.temp = j.get("main").get("temp").asDouble();
        this.wind = j.get("wind").get("speed").asDouble();
        this.clouds = j.get("clouds").get("all").asDouble();
    }

    public String getLocate() {
        return this.locate;
    }

    public String getWeather() {
        return this.weather;
    }

    public Double getTemp() {
        return this.temp;
    }

    public Double getWind() {
        return this.wind;
    }

    public Double getClouds() {
        return this.clouds;
    }

    

    

}
