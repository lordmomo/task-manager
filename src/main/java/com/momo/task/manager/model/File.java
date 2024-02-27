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
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Lob
    @Nullable
    @Column(name = "file", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private Task task;

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
