package com.idc789;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Idc789Application {
	public static void main(String[] args) {
		SpringApplication.run(Idc789Application.class, args);
	}
}
