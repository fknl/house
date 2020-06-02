package com.example.demo.repository;

import com.example.demo.model.enums.Type;
import com.example.demo.model.report.Report;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @DisplayName("Find Report List ")
    public void findReportList() {
        transactionRepository.deleteAll();
        Report save = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(5L).build());
        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 2).acquirer((long) 1).type(Type.EUR).price(5L).build());
        Report save1 = transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(2014, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.USD).price(5L).build());
        transactionRepository.save(Report.builder().date(Date.valueOf(LocalDate.of(1999, 2, 14))).merchant((long) 1).acquirer((long) 1).type(Type.EUR).price(5L).build());


        List<Report> byMerchantAndAcquirerAndDateBetween = transactionRepository.findByMerchantAndAcquirerAndDateBetween(1L, 1L, Date.valueOf(LocalDate.of(2000, 2, 14)), Date.valueOf(LocalDate.of(2020, 2, 14)));
        assertThat(byMerchantAndAcquirerAndDateBetween, hasSize(2));
        assertTrue(byMerchantAndAcquirerAndDateBetween.equals(Arrays.asList(save, save1)));
    }
}
