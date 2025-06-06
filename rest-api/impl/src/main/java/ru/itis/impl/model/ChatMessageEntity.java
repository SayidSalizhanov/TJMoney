package ru.itis.impl.model;

import jakarta.persistence.*;
import lombok.*;
import ru.itis.dto.socket.MessageType;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_messages")
@ToString(exclude = {"user", "group"})
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column
    private LocalDateTime datetime;

    //----------

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
