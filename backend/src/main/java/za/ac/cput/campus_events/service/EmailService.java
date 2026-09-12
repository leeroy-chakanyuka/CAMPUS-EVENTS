package za.ac.cput.campus_events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String email, String pin) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Campus Events Account Verification");

        message.setText(
                "Welcome to Campus Events!\n\n" +
                        "Your verification PIN is:\n\n" +
                        pin +
                        "\n\nThis PIN expires in 10 minutes."
        );

        mailSender.send(message);
    }
}
