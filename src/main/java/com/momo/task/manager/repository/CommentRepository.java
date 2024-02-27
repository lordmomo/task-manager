package com.momo.task.manager.repository;

import com.momo.task.manager.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM COMMENT c where c.task_id = :taskId"
            )
    List<Comment> findAllByCommentByTaskId(Long taskId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE comment c " +
                    "SET c.active_flg = 0 , " +
                    "c.updated_flg = 1, " +
                    "c.updated_date = NOW(), " +
                    "c.end_date = CURRENT_DATE " +
                    "WHERE c.comment_id = :commentId")
    void deleteByCommentId(Long commentId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE comment c " +
                    "SET c.active_flg = 0 , " +
                    "c.updated_flg = 1, " +
                    "c.updated_date = NOW(), " +
                    "c.end_date = CURRENT_DATE " +
                    "WHERE c.task_id = :taskId")
    void deleteByTaskId(Long taskId);
}
