package com.oauth2.ws.services.email;

import com.oauth2.ws.models.User;
import com.oauth2.ws.models.VerificationToken;

import javax.mail.internet.MimeMessage;

public interface EmailService {

    void sendHtmlEmail(MimeMessage msg);

    void sendConfirmationHtmlEmail(User user, VerificationToken vToken);
}
