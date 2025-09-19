package com.languageproject.v1.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.languageproject.v1.dto.JwtTokenLink;
import com.languageproject.v1.dto.UserSignUpDTO;
import com.languageproject.v1.exceptions.EmailMessagingException;
import com.languageproject.v1.services.CaptchaService;
import com.languageproject.v1.services.EmailService;
import com.languageproject.v1.services.SignUpTokenService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("language-app/v1/")
public class UserController {

    private CaptchaService captchaService;
    private EmailService emailService;
    private SignUpTokenService signUpTokenService;

    

    public UserController(
        CaptchaService captchaService,
        EmailService emailService,
        SignUpTokenService signUpTokenService) {
        this.captchaService = captchaService;
        this.emailService = emailService;
        this.signUpTokenService = signUpTokenService;
    }


    @PostMapping("sign-up")
    public Mono<ResponseEntity<Object>> signUpUser(
        @Valid @RequestBody UserSignUpDTO userSignUpDTO,
        @RequestParam(name = "recaptchaToken") String token){

            return this.captchaService.captchaValidation(token)
            .flatMap(isValid -> {
                if(isValid) {
                    
                    return this.emailService.sendSignUpEmail(userSignUpDTO.getEmail(), 
                    "IMPORTANT", 
                    userSignUpDTO.getEmail())
                    .thenReturn(ResponseEntity.ok().<Object>body(userSignUpDTO))
                    .onErrorResume(EmailMessagingException.class, e -> 
                    Mono.just(ResponseEntity.internalServerError()
                    .<Object>body("Failed to send email" + e.getMessage())));
                } else {
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid captcha"));

                }
            });

        
    }





    @PostMapping("validation")
    public Mono<ResponseEntity<Boolean>> validateLink(@RequestBody JwtTokenLink jwtTokenLink) {

        return signUpTokenService.IsTokenValid(jwtTokenLink.getToken())
        .map(result -> ResponseEntity.ok().body(result));

        

    }

}
