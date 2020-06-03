package com.application.alledrogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class AlledrogoApplication {

	public static void main(String[] args) {
		System.out.println(SpringVersion.getVersion());
		SpringApplication.run(AlledrogoApplication.class, args);
		System.out.println(SpringVersion.getVersion());

	}

}
