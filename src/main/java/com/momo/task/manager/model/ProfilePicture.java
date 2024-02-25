package com.momo.task.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_picture_id")
    private Long profilePictureId;

    @Lob
    @Column(name = "profileImage", columnDefinition = "LONGBLOB")
    private byte[] pictureData;

    @Transient
    @JsonIgnore
    private MultipartFile pictureFile;
}
