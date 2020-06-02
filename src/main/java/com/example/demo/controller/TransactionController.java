package com.example.demo.controller;

import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.Type;
import com.example.demo.model.report.Report;
import com.example.demo.model.report.ReportRequest;
import com.example.demo.model.report.ReportResponse;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@CrossOrigin
@RestController
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;

    @PostMapping("/transactions/report")
    public ResponseEntity<?> getReport(@RequestBody ReportRequest request) {
        List<Report> queryResult = transactionRepository.findByMerchantAndAcquirerAndDateBetween(request.getMerchant(), request.getAcquirer(), request.getFromDate(), request.getToDate());
        Map<Type, List<Report>> collect = queryResult.stream().collect(Collectors.groupingBy(x -> x.getType()));

        Stream<ReportResponse.ReportData> reportDataStream = collect.entrySet().stream().
                map(x -> new ReportResponse.ReportData().builder()
                        .count(((long) x.getValue().size()))
                        .currency(x.getKey().name())
                        .total(x.getValue().stream().flatMapToLong(y -> LongStream.of(y.getPrice())).sum())
                        .build());
        List<ReportResponse.ReportData> result = reportDataStream.collect(Collectors.toList());
        return new ResponseEntity<ReportResponse>(new ReportResponse().builder().reportData(result).status(Status.APPROVED).build(), HttpStatus.ACCEPTED);

    }
}
