package com.example.demo.repository;

import com.example.demo.model.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Report, Long> {
    List<Report> findByMerchantAndAcquirerAndDateBetween(Long merchant, Long acquire, Date startDate, Date endDate);

}
