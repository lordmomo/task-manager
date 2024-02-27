package com.momo.task.manager.repository;

import com.momo.task.manager.model.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture,Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE profile_picture p " +
                    "SET p.active_flg = 0 , " +
                    "p.updated_flg = 1, " +
                    "p.updated_date = NOW(), " +
                    "p.end_date = CURRENT_DATE " +
                    "WHERE p.profile_picture_id = :profilePictureId ")
    void deleteByPictureId(Long profilePictureId);
}
