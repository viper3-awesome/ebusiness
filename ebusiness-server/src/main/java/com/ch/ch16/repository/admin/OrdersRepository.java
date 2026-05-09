package com.ch.ch16.repository.admin;

import com.ch.ch16.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Integer>, JpaSpecificationExecutor<OrdersEntity> {

    @Query("SELECT gt.id AS id, gt.gname AS gname, gt.grprice AS grprice, od.shoppingnum AS shoppingnum, " +
           "gt.grprice * od.shoppingnum AS smallTotal FROM OrderdetailEntity od " +
           "JOIN GoodsEntity gt ON od.goodstableId = gt.id WHERE od.orderbasetableId = :orderId")
    List<Map<String, Object>> getOrdersDetail(@Param("orderId") Integer orderId);

    @Query(value = "SELECT SUM(gt.grprice * od.shoppingnum) AS value, gy.typename AS name " +
           "FROM orderbasetable ob JOIN orderdetail od ON ob.id = od.orderbasetable_id " +
           "JOIN goodstable gt ON od.goodstable_id = gt.id " +
           "JOIN goodstype gy ON gt.goodstype_id = gy.id " +
           "WHERE ob.status = 1 AND ob.orderdate > DATE_SUB(CURDATE(), INTERVAL 1 YEAR) " +
           "GROUP BY gy.typename", nativeQuery = true)
    List<Map<String, Object>> selectOrderByType();

    @Query(value = "SELECT SUM(amount) AS totalamount, DATE_FORMAT(orderdate, '%Y-%m') AS months " +
           "FROM orderbasetable WHERE status = 1 " +
           "AND DATE_FORMAT(orderdate, '%Y-%m') BETWEEN :startDate AND :endDate " +
           "GROUP BY months ORDER BY months", nativeQuery = true)
    List<Object[]> selectOrderByMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
