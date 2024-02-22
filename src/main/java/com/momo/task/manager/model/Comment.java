package com.momo.task.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id",nullable = false)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "task_id",referencedColumnName = "task_id")
    private Task taskId;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false,referencedColumnName = "user_id")
    private User userId;

    @Column(name = "message",nullable = false)
    private String message;

    @Column(name = "commented_date",nullable = false)
    private Date messagePostDate;

    @Lob
    @Nullable
    @Column(name = "file",columnDefinition = "LONGBLOB")
    private byte[] fileData;

    @Transient
    @JsonIgnore
    private MultipartFile pictureFile;
}
