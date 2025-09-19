package com.languageproject.v1.services;

import reactor.core.publisher.Mono;

public interface SignUpTokenService {

    public Mono<String> generateToken(String email);

    public Mono<Boolean> IsTokenValid(String token);

}
