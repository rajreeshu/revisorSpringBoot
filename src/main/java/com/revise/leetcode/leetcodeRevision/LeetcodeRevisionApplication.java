package com.revise.leetcode.leetcodeRevision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LeetcodeRevisionApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LeetcodeRevisionApplication.class, args);
//		PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
//        String encodedPassword = encoder.encode("1234");
//        System.out.println("Encoded password: " + encodedPassword);
//		SpringApplication.run(LeetcodeRevisionApplication.class, args);
	}

}
