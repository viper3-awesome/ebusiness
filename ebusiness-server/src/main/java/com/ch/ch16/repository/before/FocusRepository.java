package com.ch.ch16.repository.before;

import com.ch.ch16.entity.FocusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FocusRepository extends JpaRepository<FocusEntity, Integer>, JpaSpecificationExecutor<FocusEntity> {
}
