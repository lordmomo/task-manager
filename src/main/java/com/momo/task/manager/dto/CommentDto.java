package com.momo.task.manager.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class CommentDto {
    Long userId;
    String message;

    @Nullable
    MultipartFile documentFile;

}
