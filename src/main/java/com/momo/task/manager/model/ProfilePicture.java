package com.momo.task.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.momo.task.manager.utils.ResourceInformation;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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

    @PastOrPresent(message = ResourceInformation.START_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "start_date")
    private LocalDate startDate;

    @FutureOrPresent(message = ResourceInformation.END_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "end_date")
    private LocalDate endDate;

    @PastOrPresent(message = ResourceInformation.UPDATE_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "updated_date")
    private LocalDate updatedDate;
}
