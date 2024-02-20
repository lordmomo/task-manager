package com.momo.task.manager.model;

import jakarta.persistence.*;
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

    @Column(name = "full_name",nullable = false)
    private String fullName;

    @Column(name = "message",nullable = false)
    private String message;

    @Column(name = "commented_date",nullable = false)
    private Date messagePostDate;

    @Lob
    @Column(name = "profileImage",columnDefinition = "LONGBLOB")
    private byte[] pictureData;

    @Transient
    private MultipartFile pictureFile;
}
