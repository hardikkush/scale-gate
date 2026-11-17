package com.scalegate.scale_gate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ScaleGateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScaleGateApplication.class, args);
	}

}
