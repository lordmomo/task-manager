package com.momo.task.manager.model;

import com.momo.task.manager.utils.ResourceInformation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @NotBlank(message = ResourceInformation.ROLE_NOT_BLANK_VALIDATION_MESSAGE)
    @Column(name = "role_name", nullable = false)
    private String roleName;
}
