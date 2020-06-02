package com.example.demo.config;

import com.example.demo.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationErrorResponse {
    private final Status status;
    private final String message;
    private Integer code;
}
