package com.findork.pdm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PdmApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdmApplication.class, args);
	}

}
