package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.model.enums.Type;
import com.example.demo.model.report.Report;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class CaseApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CaseApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(User.builder().email("demo@financialhouse.io").password(passwordEncoder().encode("cjaiU8CV")).build());

        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(5L).build());
        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2020, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(52L).build());
        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.USD).price(22L).build());
        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2020, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.USD).price(111L).build());
    }
}
