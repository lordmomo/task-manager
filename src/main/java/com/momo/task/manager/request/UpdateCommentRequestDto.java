package com.momo.task.manager.request;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateCommentRequestDto {
    String message;

    @Nullable
    MultipartFile documentFile;
}
