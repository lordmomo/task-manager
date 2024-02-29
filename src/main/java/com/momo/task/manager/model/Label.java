package com.momo.task.manager.model;

import com.momo.task.manager.utils.ResourceInformation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Long labelId;

    @NotBlank(message = ResourceInformation.TASK_LABEL_REQUIRED_MESSAGE)
    @Column(name = "label_name",nullable = false)
    private String labelName;

}
