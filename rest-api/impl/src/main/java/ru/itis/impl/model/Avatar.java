package ru.itis.impl.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avatars")
@Builder
@ToString(exclude = {"user"})
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Builder.Default
    private String url = "/images/defaultAvatar.png";

    //----------

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
