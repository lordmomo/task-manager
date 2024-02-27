package com.momo.task.manager.model;

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
public class Access {

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

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

}
