package com.languageproject.v1.dto;

import lombok.Data;

@Data
public class RecaptchaResponse {

    private boolean success;

    private String challengeTs;

    private String hostname;

    private String[] errorCodes;

    public boolean isSuccess() {
        return this.success;
    }

}
