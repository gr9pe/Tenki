package com.tenki.tenki;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResponseReader {
    HttpURLConnection con;
    public ResponseReader(){
        this.con = con;
    }
    String read(HttpURLConnection con){
        String result = "";
        try{BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
            String tmp = "";
            while ((tmp = in.readLine()) != null) {
                result += tmp;
            }
            return result;
        }catch(Exception e){
            System.err.println(e);
        }
        return result;
    }
   
}
