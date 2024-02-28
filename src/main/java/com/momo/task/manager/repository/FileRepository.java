package com.momo.task.manager.repository;

import com.momo.task.manager.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FileRepository extends JpaRepository<File,Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "UPDATE file f " +
            "SET f.active_flg = 0 , " +
            "f.updated_flg = 1, " +
            "f.updated_date = NOW(), " +
            "f.end_date = CURRENT_DATE " +
            "WHERE f.task_id = :taskId")
    void deleteByTaskId(Long taskId);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM file f " +
                    "WHERE f.task_id = :taskId")
    File findFileByTaskId(Long taskId);

}


