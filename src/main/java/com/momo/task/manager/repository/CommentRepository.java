package com.momo.task.manager.repository;

import com.momo.task.manager.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM COMMENT c where c.task_id = :taskId"
            )
    List<Comment> findAllByCommentByTaskId(Long taskId);

}
