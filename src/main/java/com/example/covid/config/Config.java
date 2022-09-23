package com.example.covid.config;

import com.example.covid.repository.EventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    EventRepository eventRepository() {
        return new EventRepository() {};
    }
}
