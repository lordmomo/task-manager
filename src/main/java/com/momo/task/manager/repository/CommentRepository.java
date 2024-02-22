package com.momo.task.manager.repository;

import com.momo.task.manager.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query(nativeQuery = true,value = "DELETE FROM COMMENT c WHERE c.task_id = :taskId AND c.fullName = :fullName ")
    void deleteByTaskAndName(String fullName, Long taskId);
}
