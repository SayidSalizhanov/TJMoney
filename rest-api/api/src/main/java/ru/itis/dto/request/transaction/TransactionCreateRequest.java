package ru.itis.dto.request.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionCreateRequest (
        Integer amount,
        String type,
        String category,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime dateTime,
        String description
) {
}
