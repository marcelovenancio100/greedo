package com.oauth2.ws.config;

import com.oauth2.ws.services.email.EmailService;
import com.oauth2.ws.services.email.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfig {

    @Bean
    public EmailService emailService() {
        return new SmtpEmailService();
    }
}
