package com.momo.task.manager.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role", referencedColumnName = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "profile_image", referencedColumnName = "profile_picture_id")
    @Nullable
    private ProfilePicture picture;

    @Column(name = "active_flg")
    private boolean activeFlg;

    @Column(name = "updated_flg")
    private boolean updatedFlg;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    public String fullName() {
        return firstName + " " + lastName;
    }


}
