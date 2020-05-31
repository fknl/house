package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.example.demo.model.enums.Status;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationErrorResponse {
    private final Status status;
    private final String message;
    private Integer code;
}
