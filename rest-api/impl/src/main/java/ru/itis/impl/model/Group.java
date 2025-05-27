package ru.itis.impl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups")
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private String description;

    //----------

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<GroupMember> groupMembers;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<Application> applications = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<Goal> goals = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<Record> records = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<Reminder> reminders;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<Transaction> transactions;
}