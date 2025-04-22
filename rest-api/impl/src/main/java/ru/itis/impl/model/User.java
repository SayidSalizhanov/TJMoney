package ru.itis.impl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
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
    private Set<GroupMember> groupMembers;

    @OneToOne(mappedBy = "user")
    private Avatar avatar;

    @OneToMany(mappedBy = "user")
    private Set<Application> applications = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Goal> goals = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Record> records = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Reminder> reminders;

    @OneToMany(mappedBy = "user")
    private Set<Transaction> transactions;
}