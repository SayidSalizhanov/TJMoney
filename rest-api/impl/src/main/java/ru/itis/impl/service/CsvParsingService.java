package ru.itis.impl.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsvParsingService {
    List<Long> parseCsv(Long groupId, MultipartFile file);
}
