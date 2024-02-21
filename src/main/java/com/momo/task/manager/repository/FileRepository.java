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
            value = "DELETE FROM file f WHERE f.task_id = :taskId" )
    void deleteByTaskId(Long taskId);
}


