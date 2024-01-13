package com.dontbe.www.DontBeServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DontBeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DontBeServerApplication.class, args);
	}

}
