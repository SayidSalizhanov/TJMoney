package ru.itis.impl.service;

import java.util.Map;

public interface FxRatesApiService {
    Map<String, Double> getLatestRates(String base);
    Map<String, Double> getLatestRates(String base, String... symbols);
}
