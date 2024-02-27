package com.momo.task.manager.repository;

import com.momo.task.manager.model.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccessRepository extends JpaRepository<Access,Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE access a " +
                    "SET a.active_flg = 0 , " +
                    "a.updated_flg = 1, " +
                    "a.updated_date = NOW(), " +
                    "a.end_date = CURRENT_DATE " +
                    "WHERE a.accessed_user_id = :userId ")
    void deleteByUserId (Long userId);


    @Query(nativeQuery = true,
            value = "select * " +
                    "FROM access a where a.accessed_user_id = :userId AND a.accessed_project_id = :projectId ;"
    )
    Access validateUserProjectRelation(Long userId,Long projectId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE access a " +
                    "SET a.active_flg = 0 , " +
                    "a.updated_flg = 1, " +
                    "a.updated_date = NOW(), " +
                    "a.end_date = CURRENT_DATE " +
                    "WHERE a.accessed_project_id = :projectId ")
    void deleteProjectById(Long projectId);
}
