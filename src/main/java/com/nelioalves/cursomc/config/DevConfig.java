package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.mail.EmailService;
import com.nelioalves.cursomc.services.mail.SmtpEmailService;
import com.nelioalves.cursomc.services.test.DBServiceTest;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBServiceTest dbServiceTeste;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		
		if(!"create".equals(strategy)) {
			return false;
		}
		
		dbServiceTeste.instantiateTesteDataBase();
		
		return true;
	}
	
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
	
}
