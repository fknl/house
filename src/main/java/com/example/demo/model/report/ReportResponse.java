package com.example.demo.model.report;

import com.example.demo.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class ReportResponse {
    @JsonProperty("status")
    private Status status;

    @JsonProperty("data")
    private List<ReportData> reportData;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(access = AccessLevel.PUBLIC)
    public static class ReportData {
        @JsonProperty("count")
        private Long count;

        @JsonProperty("total")
        private Long total;

        @JsonProperty("currency")
        private String currency;
    }
}
