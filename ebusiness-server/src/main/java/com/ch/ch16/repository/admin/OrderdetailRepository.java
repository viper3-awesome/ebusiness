package com.ch.ch16.repository.admin;

import com.ch.ch16.entity.OrderdetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderdetailRepository extends JpaRepository<OrderdetailEntity, Integer>, JpaSpecificationExecutor<OrderdetailEntity> {
}
