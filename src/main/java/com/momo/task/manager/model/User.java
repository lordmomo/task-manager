package com.momo.task.manager.model;

import com.momo.task.manager.utils.ConstantInformation;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @NotBlank(message = ConstantInformation.FIRST_NAME_REQUIRED_MESSAGE)
    @Column(name = "first_name", nullable = false)
    @Length(min = 3, max = 16, message = ConstantInformation.FIRST_NAME_LENGTH_MESSAGE)
    private String firstName;
    @NotBlank(message = ConstantInformation.LAST_NAME_REQUIRED_MESSAGE)
    @Column(name = "last_name", nullable = false)
    @Length(min = 3, max = 16, message = ConstantInformation.LAST_NAME_LENGTH_MESSAGE)
    private String lastName;

    @Email(message = ConstantInformation.INVALID_EMAIL_FORMAT_MESSAGE)
    @NotBlank(message = ConstantInformation.EMAIL_REQUIRED_MESSAGE)
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = ConstantInformation.USERNAME_REQUIRED_MESSAGE)
    @Column(name = "username", unique = true, nullable = false)
    @Length(min = 6, max = 16, message = ConstantInformation.USERNAME_LENGTH_MESSAGE)
    private String username;

    @NotBlank(message = ConstantInformation.PASSWORD_REQUIRED_MESSAGE)
    @Column(name = "password", nullable = false)
    @Length(min = 8, message = ConstantInformation.PASSWORD_LENGTH_MESSAGE)
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

    @PastOrPresent(message = ConstantInformation.START_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @FutureOrPresent(message = ConstantInformation.END_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @PastOrPresent(message = ConstantInformation.UPDATE_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public String fullName() {
        return firstName + " " + lastName;
    }

    public List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }


}
