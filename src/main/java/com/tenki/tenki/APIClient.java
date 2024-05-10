package com.tenki.tenki;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

@RestController
public class APIClient {

	private String API_KEY;
	private String BASE_URL;
	private ObjectMapper objectMapper;

	public APIClient(){
	}

	public APIClient(String API_KEY, String BASE_URL) {
		this.objectMapper = new ObjectMapper();
		this.API_KEY = API_KEY;
		this.BASE_URL = BASE_URL;
	}

	//APIにリクエストしてJsonNode返す
	public JsonNode getJsonNode(){
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL+API_KEY).openConnection();
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

	//ChatGPTにプロンプト渡してレスポンス内のメッセージを返す
	public String getRecommend(String prompt){
		String model = "gpt-3.5-turbo"; 
		String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
		try{
			HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL).openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			con.setRequestProperty("Authorization", "Bearer " + API_KEY);
			con.connect();

            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

			if (con.getResponseCode() == 200) { 
				String res = objectMapper.readTree(new ResponseReader().read(con)).get("choices").get(0).get("message").get("content").toString();
				return removeSymbols(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "取得できませんでした。";
	}

	public String removeSymbols(String s) {
		return s.replaceAll("[^a-zA-Z0-9\\s/\\-]", "");
	}
}
