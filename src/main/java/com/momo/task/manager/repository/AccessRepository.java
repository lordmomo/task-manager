package com.momo.task.manager.repository;

import com.momo.task.manager.model.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccessRepository extends JpaRepository<Access,Long> {

    @Modifying
    @Transactional
    @Query (nativeQuery = true,
            value = " DELETE FROM access a WHERE a.accessed_user_id = :userId")
    void deleteByUserId (Long userId);
}
