package com.example.rollbasedlogin.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    public String getWeather(String city) {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + apiKey + "&units=metric";

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            JSONObject json = new JSONObject(response);

            String condition = json.getJSONArray("weather")
                                   .getJSONObject(0)
                                   .getString("main");

            double temp = json.getJSONObject("main")
                              .getDouble("temp");

            return condition + " | " + temp + "°C";

        } catch (Exception e) {
            return "Weather unavailable";
        }
    }
}
