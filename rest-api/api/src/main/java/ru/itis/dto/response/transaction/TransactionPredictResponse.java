package ru.itis.dto.response.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class TransactionPredictResponse {
    private Map<String, Double> predicted_expenses;
    private Double total;

    public TransactionPredictResponse(Map<String, Double> predicted_expenses, Double total) {
        this.predicted_expenses = predicted_expenses;
        this.total = total;
    }

}