package com.momo.task.manager.repository;

import com.momo.task.manager.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label,Long> {
    Optional<Label> findByLabelName(String labelName);

    @Query(nativeQuery = true, value = "Select l.label_id from label l where l.label_name = :labelName")
    Long getLabelIdFromName(String labelName);
}
