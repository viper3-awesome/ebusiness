package com.ch.ch16.repository.admin;

import com.ch.ch16.entity.GoodsTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoodsTypeRepository extends JpaRepository<GoodsTypeEntity, Integer>, JpaSpecificationExecutor<GoodsTypeEntity> {
}
