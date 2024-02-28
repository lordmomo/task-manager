package com.momo.task.manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stages {

    @Id
    @Column(name = "stage_id")
    Long id;

    @NotBlank(message = "Stage name is required")
    @Column(name = "stage_name")
    String stageName;
}
