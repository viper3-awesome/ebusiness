package com.ch.ch16.repository.before;

import com.ch.ch16.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartRepository extends JpaRepository<CartEntity, Integer>, JpaSpecificationExecutor<CartEntity> {
}
