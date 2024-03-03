package com.momo.task.manager.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto implements Serializable {
    Long userId;
    String username;
    String message;
    byte[] fileData;

    @Nullable
    transient MultipartFile documentFile;

}
