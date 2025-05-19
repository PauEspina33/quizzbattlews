package com.quizzbattle.ws.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String title;

    @NotBlank
    private String body;
}
