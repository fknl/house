package com.example.demo.controller;

import com.example.demo.model.UserResponse;
import com.example.demo.model.enums.Type;
import com.example.demo.model.report.Report;
import com.example.demo.model.report.ReportRequest;
import com.example.demo.model.report.ReportResponse;
import com.example.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Merchant Controller Integration Tests")
@Execution(ExecutionMode.CONCURRENT)
public class ReportControllerIntegrationTest extends HelperTest {
    @Value("${jwt.expiration}")
    private long expiration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @DisplayName("Report List without Authentication")
    public void reportListWithError() throws URISyntaxException {
        transactionRepository.deleteAll();
        Report save = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(5L).build());
        Report save1 = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.USD).price(51L).build());
        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 2).acquirer((long) 1).type(Type.EUR).price(5L).build());
        Report save2 = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.USD).price(5L).build());
        Report save3 = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(32L).build());
        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(1999, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(5L).build());

        UserResponse response = login("demo@financialhouse.io", "cjaiU8CV");
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZW1vQGZpbmFuY2lhbGhvdXNlLmlvIiwiZXhwIjoxNTkwOTkxMzk4LCJpYXQiOjE1OTA5NzMzOTh9.HibMBxsP_u7nBD_A2Ru5D8Lm0UgmCyeC-BgR70GHF_8hjYZ4mwuvNfkWpkxSlU4f0QkJGwO0KO2YHQoEsjnWWQ";
        ReportRequest reportRequest = ReportRequest.builder().acquirer(1L).merchant(1L).fromDate(Date.valueOf(LocalDate.of(2000, 2, 14))).toDate(Date.valueOf(LocalDate.of(2030, 2, 14))).build();

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            reportList(reportRequest, token);
        });

        assertTrue(exception instanceof HttpClientErrorException);
        assertTrue(exception.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value());


    }

    @Test
    @DisplayName("Report List with Authentication")
    public void reportList() throws URISyntaxException {
        transactionRepository.deleteAll();
        Report save = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(5L).build());
        Report save1 = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.USD).price(51L).build());
        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 2).acquirer((long) 1).type(Type.EUR).price(5L).build());
        Report save2 = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.USD).price(5L).build());
        Report save3 = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(32L).build());
        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(1999, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(5L).build());

        UserResponse response = login("demo@financialhouse.io", "cjaiU8CV");
        String token = response.getToken();
        ReportRequest reportRequest = ReportRequest.builder().acquirer(1L).merchant(1L).fromDate(Date.valueOf(LocalDate.of(2000, 2, 14))).toDate(Date.valueOf(LocalDate.of(2030, 2, 14))).build();
        ReportResponse reportResponse = reportList(reportRequest, token);


        assertEquals(reportResponse.getReportData().stream().count(), 2);
        Map<String, List<ReportResponse.ReportData>> collect = reportResponse.getReportData().stream().collect(Collectors.groupingBy(x -> x.getCurrency()));

        assertTrue(collect.get("EUR").get(0).getCount() == 2);
        assertTrue(collect.get("EUR").get(0).getTotal() == 37);
        assertTrue(collect.get("USD").get(0).getCount() == 2);
        assertTrue(collect.get("USD").get(0).getTotal() == 56);

    }


}
