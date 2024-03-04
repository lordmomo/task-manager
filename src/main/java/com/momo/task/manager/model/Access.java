package com.momo.task.manager.model;

import com.momo.task.manager.utils.ResourceInformation;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "access")
public class Access implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id", nullable = false)
    private Long accessId;

    @ManyToOne
    @JoinColumn(name = "accessed_user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "accessed_project_id", referencedColumnName = "project_id")
    private Project project;


    @Column(name = "active_flg")
    private boolean activeFlg;

    @Column(name = "updated_flg")
    private boolean updatedFlg;

    @PastOrPresent(message = ResourceInformation.UPDATE_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PastOrPresent(message = ResourceInformation.START_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @FutureOrPresent(message = ResourceInformation.END_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "end_date")
    private LocalDateTime endDate;

}
