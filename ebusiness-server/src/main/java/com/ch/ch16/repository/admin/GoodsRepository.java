package com.ch.ch16.repository.admin;

import com.ch.ch16.entity.GoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface GoodsRepository extends JpaRepository<GoodsEntity, Integer>, JpaSpecificationExecutor<GoodsEntity> {

    @Query("SELECT gt, gy.typename FROM GoodsEntity gt JOIN GoodsTypeEntity gy ON gy.id = gt.goodstypeId WHERE gt.id IN " +
           "(SELECT f.goodstableId FROM FocusEntity f WHERE f.busertableId = :uid)")
    Page<GoodsEntity> findMyFocusGoods(@Param("uid") Integer busertableId, Pageable pageable);

    @Query("SELECT gt, ct.shoppingnum, ct.id AS cid FROM GoodsEntity gt " +
           "JOIN CartEntity ct ON ct.goodstableId = gt.id WHERE ct.busertableId = :uid")
    List<Map<String, Object>> myCartGoods(@Param("uid") Integer uid);
}
