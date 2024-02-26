package com.momo.task.manager.repository;

import com.momo.task.manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperAdminRepository extends JpaRepository<User,Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM USER u where u.role = :role")
    List<User> findUsersByRole(Long role);
}
