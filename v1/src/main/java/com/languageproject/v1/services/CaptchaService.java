package com.languageproject.v1.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.languageproject.v1.dto.RecaptchaResponse;

import reactor.core.publisher.Mono;

@Service
public class CaptchaService {

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    private static final String SECRET_KEY = "6LekWi8rAAAAAKVYzLEZRS8DuX_qUGNrwxPyzT0J";

    private final WebClient webClient;

    public CaptchaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(RECAPTCHA_VERIFY_URL).build();

    }

    public Mono<Boolean> captchaValidation(String token) {
        return webClient.post()
        .uri("")
        .body(BodyInserters.fromFormData("secret", SECRET_KEY).with("response", token))
        .retrieve()
        .bodyToMono(RecaptchaResponse.class)
        .map(RecaptchaResponse::isSuccess)
        .onErrorReturn(false);

    }

}
