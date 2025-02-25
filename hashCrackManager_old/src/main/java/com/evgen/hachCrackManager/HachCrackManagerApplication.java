package com.evgen.hachCrackManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class HachCrackManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HachCrackManagerApplication.class, args);
	}

}
