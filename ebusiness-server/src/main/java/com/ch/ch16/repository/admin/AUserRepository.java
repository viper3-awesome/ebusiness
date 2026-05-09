package com.ch.ch16.repository.admin;

import com.ch.ch16.entity.AUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AUserRepository extends JpaRepository<AUserEntity, Integer>, JpaSpecificationExecutor<AUserEntity> {
}
