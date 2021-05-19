package com.vishal.personal.vaccinealert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class VaccineAlertApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccineAlertApplication.class, args);
	}

}
