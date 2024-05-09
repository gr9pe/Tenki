package com.tenki.tenki;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

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
            return "result";
        }
        return "fail";
    }
   
    @ModelAttribute
    public Tenki getWeather(String city){
        JsonNode json = new APIClient().callAPI(city);
        if(json != null){
            Tenki tenki = new Tenki(json);
            return tenki != null ? tenki : null;
        }else{
            return null;
        } 
    }

    
}
