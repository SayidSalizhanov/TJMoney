package ru.itis.dto.response.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Setter
@Getter
@ToString
public class TransactionPredictResponse {
    private Map<String, Double> predicted_expenses;
    private Double total;
    private String message;
    private Map<String, Double> historical_expenses;

    public TransactionPredictResponse() {
        // Конструктор по умолчанию для Jackson
    }

    public TransactionPredictResponse(Map<String, Double> predicted_expenses, Double total, String message) {
        this.predicted_expenses = predicted_expenses;
        this.total = total;
        this.message = message;
    }

    public TransactionPredictResponse(Map<String, Double> predicted_expenses, Double total, String message, Map<String, Double> historical_expenses) {
        this.predicted_expenses = predicted_expenses;
        this.total = total;
        this.message = message;
        this.historical_expenses = historical_expenses;
    }
}