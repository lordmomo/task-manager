package com.momo.task.manager.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.momo.task.manager.utils.CustomCommentSerializer;
import com.momo.task.manager.utils.ConstantInformation;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize(using = CustomCommentSerializer.class)
@Table(name = "comment")
//@RedisHash("CommentHash")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private  Task taskId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private  User userId;

    @Column(name = "message", nullable = false)
    private String message;

    @PastOrPresent(message = ConstantInformation.MESSAGE_POSTED_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "commented_date", nullable = false)
    private LocalDateTime messagePostDate;

    @Lob
    @Nullable
    @Column(name = "file", columnDefinition = "LONGBLOB")
    private  byte[] fileData;

    @Column(name = "active_flg")
    private boolean activeFlg;

    @Column(name = "updated_flg")
    private boolean updatedFlg;

    @PastOrPresent(message = ConstantInformation.UPDATE_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PastOrPresent(message = ConstantInformation.START_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @FutureOrPresent(message = ConstantInformation.END_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "end_date")
    private LocalDateTime endDate;


}
