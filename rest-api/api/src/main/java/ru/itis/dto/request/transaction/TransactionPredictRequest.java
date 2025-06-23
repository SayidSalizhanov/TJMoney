package ru.itis.dto.request.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class TransactionPredictRequest {
    @Setter
    @Getter
    public static class MonthData {
        private int month;
        private Map<String, Integer> categories;

        public MonthData(int month, Map<String, Integer> categories) {
            this.month = month;
            this.categories = categories;
        }

    }

    private List<MonthData> months;

    public TransactionPredictRequest(List<MonthData> months) {
        this.months = months;
    }

}