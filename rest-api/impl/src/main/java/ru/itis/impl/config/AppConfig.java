package ru.itis.impl.config;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.itis.impl.properties.CloudinaryProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(CloudinaryProperties.class)
@RequiredArgsConstructor
public class AppConfig {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final CloudinaryProperties cloudinaryProperties;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudinaryProperties.getCloudName());
        config.put("api_key", cloudinaryProperties.getApiKey());
        config.put("api_secret", cloudinaryProperties.getApiSecret());
        return new Cloudinary(config);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(formatter));

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
