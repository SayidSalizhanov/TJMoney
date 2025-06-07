package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.impl.service.FxRatesApiService;

import java.util.Map;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final FxRatesApiService fxRatesApiService;

    @GetMapping("/latest")
    public ResponseEntity<Map<String, Double>> getLatestRates(
            @RequestParam(defaultValue = "USD") String base) {
        return ResponseEntity.ok(fxRatesApiService.getLatestRates(base));
    }

    @GetMapping("/latest/specific")
    public ResponseEntity<Map<String, Double>> getSpecificRates(
            @RequestParam(defaultValue = "USD") String base,
            @RequestParam String... symbols) {
        return ResponseEntity.ok(fxRatesApiService.getLatestRates(base, symbols));
    }

    @GetMapping("/convert")
    public ResponseEntity<Double> convertCurrency(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam Double amount) {
        Map<String, Double> rates = fxRatesApiService.getLatestRates(from, to);
        Double rate = rates.get(to);
        if (rate == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(amount * rate);
    }
} 