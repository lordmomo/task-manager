package com.momo.task.manager.repository;

import com.momo.task.manager.model.Stages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StagesRepository extends JpaRepository<Stages,Long> {
}
