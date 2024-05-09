package com.tenki.tenki;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

@RestController
public class APIClient {

	private String API_KEY;
	private ObjectMapper objectMapper;

	public APIClient() {
		this.objectMapper = new ObjectMapper();
		this.API_KEY = Dotenv.configure().load().get("TENKI_API_KEY");
	}

	public JsonNode callAPI(String city){
		StringBuilder url = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?q=")
			.append(city)
			.append("&appid=").append(API_KEY)
			.append("&units=metric&lang=ja");
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url.toString()).openConnection(); 
			con.setRequestMethod("GET"); 
			con.connect();
			if (con.getResponseCode() == 200) { 
				return objectMapper.readTree(new ResponseReader().read(con));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
