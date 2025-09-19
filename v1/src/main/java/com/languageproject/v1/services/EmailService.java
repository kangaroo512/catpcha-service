package com.languageproject.v1.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.languageproject.v1.exceptions.EmailMessagingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class EmailService {

    private SignUpTokenService signUpTokenService;
    private JavaMailSender mailSender;
    private String frontEndBaseUrl;
    private TemplateEngine templateEngine;

    

    public EmailService(
        SignUpTokenService signUpTokenService,
        JavaMailSender javaMailSender,
        TemplateEngine templateEngine,
        @Value("${frontEndBaseUrl}") String frontEndBaseUrl
    ) {
        this.signUpTokenService = signUpTokenService;
        this.mailSender = javaMailSender;
        this.frontEndBaseUrl = frontEndBaseUrl;
        this.templateEngine = templateEngine;
    }

    public Mono<Void> sendSignUpEmail(
        String to, 
        String subject, 
        String email) {

            return Mono.zip(loadTemplate(), linkGenerator(email))
            .map(tuple -> {
                String confirmationLink = tuple.getT2();
                return confirmationLink;
            })
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(verificationLink -> {
                try {

                    Context context = new Context();
                    context.setVariable("verificationLink", verificationLink);

                    String html = templateEngine.process("email-template", context);



                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                    helper.setTo(to);
                    helper.setSubject(subject);
                    helper.setText(html, true);

                    mailSender.send(message);
                    
                } catch (MessagingException e) {
                    throw new EmailMessagingException("Failed to send email", e);
                }
            })
            .then();

    }

    private Mono<String> loadTemplate() {

        return Mono.fromCallable(() -> {
            try {
                Path path = Paths.get("src/main/resources/static/messageEmail.html");
                return Files.readString(path, StandardCharsets.UTF_8);
                
            } catch (Exception e) {
                throw new RuntimeException("Failed to load template", e);
            }
        }).subscribeOn(Schedulers.boundedElastic());
        



    }

    private Mono<String> linkGenerator(String email) {
        return signUpTokenService.generateToken(email)
        .map(jwt -> {
            String encodedToken = URLEncoder.encode(jwt, StandardCharsets.UTF_8);

            String fullUrl = frontEndBaseUrl + "create-account?token=" + encodedToken;

            return fullUrl;
        });

    }

}
