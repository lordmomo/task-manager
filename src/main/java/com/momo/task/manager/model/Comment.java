package com.momo.task.manager.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.momo.task.manager.utils.CustomCommentSerializer;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize(using = CustomCommentSerializer.class)
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private Task taskId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User userId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "commented_date", nullable = false)
    private Date messagePostDate;

    @Lob
    @Nullable
    @Column(name = "file", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    @Column(name = "active_flg")
    private boolean activeFlg;

    @Column(name ="updated_flg")
    private boolean updatedFlg;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;


}
