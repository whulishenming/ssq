package com.lsm.ssq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:quartz-scheduled.xml")
public class SsqApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsqApplication.class, args);
	}
}
