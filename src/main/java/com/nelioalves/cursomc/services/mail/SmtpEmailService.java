package com.nelioalves.cursomc.services.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService{
	
	@Autowired
	MailSender mailSender;
	

	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage simpleMailMessage) {
		LOG.info("Enviando email.... ");
		
		mailSender.send(simpleMailMessage);
		
		LOG.info("Email Enviado!");
		
	}

}
