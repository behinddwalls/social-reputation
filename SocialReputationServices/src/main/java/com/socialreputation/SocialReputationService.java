package com.socialreputation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;

/**
 * account: socialreputation.com@gmail.com
 * 
 * @author preetam
 *
 */
@SpringBootApplication
@EnableRetry
@PropertySource("classpath:aws.properties")
public class SocialReputationService {


	public static void main(String[] args) {

		SpringApplication.run(SocialReputationService.class, args);
	}

	
}