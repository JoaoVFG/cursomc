package com.nelioalves.cursomc.services.mail;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService{
	

	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

	
	@Override
	public void sendEmail(SimpleMailMessage simpleMailMessage) {
		LOG.info("Simulando envio de email.... ");
		LOG.info(simpleMailMessage.toString());
		LOG.info("Email Enviado!");
		
	}


	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Simulando envio de email HTML.... ");
		LOG.info(msg.toString());
		LOG.info("Email Enviado!");
	}

}
