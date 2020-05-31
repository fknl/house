package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.demo.model.enums.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String token;
    private Status status;

}
