package com.momo.task.manager.model;

import jakarta.persistence.*;
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

    @Column(name = "stage_name")
    String stageName;
}
