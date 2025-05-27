package ru.itis.impl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
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

    //----------

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Avatar avatar;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Reminder> reminders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();
}