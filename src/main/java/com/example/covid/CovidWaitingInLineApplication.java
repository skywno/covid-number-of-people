package com.example.covid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CovidWaitingInLineApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidWaitingInLineApplication.class, args);
	}

}
