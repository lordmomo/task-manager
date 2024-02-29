package com.momo.task.manager.repository;

import com.momo.task.manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuperAdminRepository extends JpaRepository<User,Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM USER u where u.role = :role AND u.active_flg = 1")
    List<User> findUsersByRole(Long role);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE user u " +
                    "SET u.active_flg = 0 , " +
                    "u.updated_flg = 1, " +
                    "u.updated_date = NOW(), " +
                    "u.end_date = NOW() " +
                    "WHERE u.user_id = :userId ")
    void deleteByUserId(Long userId);

    Optional<User> findByUsername(String username);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM user u " +
                    "WHERE u.active_flg = 1")
    List<User> findAllActiveUsers();
}
