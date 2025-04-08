package ru.itis.impl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "telegram_id")
    private String telegramId;

    @Column(name = "sending_to_telegram")
    private Boolean sendingToTelegram;

    @Column(name = "sending_to_email")
    private Boolean sendingToEmail;
}