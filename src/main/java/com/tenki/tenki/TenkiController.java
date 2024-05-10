package com.tenki.tenki;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.ui.Model;    

@Controller
public class TenkiController {
    @GetMapping("/")
    public String getForm(Model model){
        return "form";
    }

    @PostMapping
    public String getResult(@RequestParam(name = "city", defaultValue = "tokyo") String city, Model model){
        Tenki tenki = getWeather(city);
        if(tenki !=  null){
            model.addAttribute("tenki",tenki);
            model.addAttribute("music",getRecommend(tenki.getWeather()));
            return "result";
        }
        return "fail";
    }
   
    @ModelAttribute
    public Tenki getWeather(String city){
        String url = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?q=").append(city).append("&appid=").toString();
        String TENKI_API_KEY = Dotenv.configure().load().get("TENKI_API_KEY");
        JsonNode json = new APIClient(TENKI_API_KEY,url).getJsonNode();
        if(json != null){
            Tenki tenki = new Tenki(json);
            return tenki != null ? tenki : null;
        }else{
            return null;
        } 
    }

    public String getRecommend(String weather){
        String url = "https://api.openai.com/v1/chat/completions";
        APIClient openAI = new APIClient(Dotenv.configure().load().get("OPENAI_API_KEY"),url);
        return openAI.getRecommend("Today's weather:"+ weather +". Please tell me a song that matches today's weather. Please answer with only the song title and artist name, without line breaks.") ;
    }
    
}
