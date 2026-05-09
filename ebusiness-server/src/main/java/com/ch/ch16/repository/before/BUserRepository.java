package com.ch.ch16.repository.before;

import com.ch.ch16.entity.BUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BUserRepository extends JpaRepository<BUserEntity, Integer>, JpaSpecificationExecutor<BUserEntity> {
}
