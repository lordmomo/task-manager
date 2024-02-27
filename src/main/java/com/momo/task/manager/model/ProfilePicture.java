package com.momo.task.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "profile_picture")
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

    @Column(name = "active_flg")
    private boolean activeFlg;

    @Column(name = "updated_flg")
    private boolean updatedFlg;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;
}
