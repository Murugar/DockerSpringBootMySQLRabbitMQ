package com.iqmsoft.docker.rabbit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class SpringRabbitApplication implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(SpringRabbitApplication.class);


	@Override
	public void run(String... args) throws Exception {
		logger.info("Main ");
		logger.debug("Main ");
	
	}

	
	public static void main(String[] args) {

		SpringApplication.run(SpringRabbitApplication.class, args);
	}

}
