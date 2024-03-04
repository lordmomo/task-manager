package com.momo.task.manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stages")
public class Stages implements Serializable {

    @Id
    @Column(name = "stage_id")
    Long id;

    @NotBlank(message = "Stage name is required")
    @Column(name = "stage_name")
    String stageName;
}
