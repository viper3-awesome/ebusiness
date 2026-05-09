package com.ch.ch16.service.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.*;
import com.ch.ch16.repository.admin.OrdersRepository;
import com.ch.ch16.service.before.CartService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@SuppressWarnings("all")
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderdetailService orderdetailService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CartService cartService;

    @Override
    public ResponseResult<Map<String, Object>> getAllOrders(OrdersEntity ordersEntity) {
        int pageSize = ordersEntity.getPageSize() != null ? ordersEntity.getPageSize() : 5;
        int currentPage = ordersEntity.getCurrentPage() != null ? ordersEntity.getCurrentPage() : 1;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Specification<OrdersEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (ordersEntity.getId() != null && ordersEntity.getId() > 0)
                predicates.add(cb.equal(root.get("id"), ordersEntity.getId()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        var page = ordersRepository.findAll(spec, pageable);
        Map<String, Object> myres = new HashMap<>();
        myres.put("allOrders", page.getContent());
        myres.put("totalPage", page.getTotalPages());
        return ResponseResult.getSuccessResult(myres);
    }

    @Override
    public ResponseResult<Map<String, Object>> getOrdersByUid(OrdersEntity ordersEntity) {
        int pageSize = ordersEntity.getPageSize() != null ? ordersEntity.getPageSize() : 5;
        int currentPage = ordersEntity.getCurrentPage() != null ? ordersEntity.getCurrentPage() : 1;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Specification<OrdersEntity> spec = (root, query, cb) ->
                cb.equal(root.get("busertableId"), ordersEntity.getBusertableId());

        var page = ordersRepository.findAll(spec, pageable);
        Map<String, Object> myres = new HashMap<>();
        myres.put("ordersByUid", page.getContent());
        myres.put("totalPage", page.getTotalPages());
        return ResponseResult.getSuccessResult(myres);
    }

    @Override
    public ResponseResult<List<Map<String, Object>>> getOrdersDetail(OrdersEntity ordersEntity) {
        return ResponseResult.getSuccessResult(ordersRepository.getOrdersDetail(ordersEntity.getId()));
    }

    @Override
    public ResponseResult<Map<String, String>> goPay(OrdersEntity ordersEntity) {
        ordersEntity.setStatus(1);
        ordersRepository.save(ordersEntity);
        return ResponseResult.getMessageResult(null, "A001");
    }

    @Override
    public ResponseResult<List<OrdersEntity>> selectOrderByMonth(OrdersEntity ordersEntity) {
        if (ordersEntity.getStartDate() == null || ordersEntity.getStartDate().length() == 0)
            ordersEntity.setStartDate("1900-01");
        if (ordersEntity.getEndDate() == null || ordersEntity.getEndDate().length() == 0)
            ordersEntity.setEndDate("9000-12");

        List<Object[]> results = ordersRepository.selectOrderByMonth(
                ordersEntity.getStartDate(), ordersEntity.getEndDate());
        List<OrdersEntity> ordersEntityList = new ArrayList<>();
        for (Object[] row : results) {
            OrdersEntity oe = new OrdersEntity();
            oe.setTotalamount(((Number) row[0]).doubleValue());
            oe.setMonths((String) row[1]);
            ordersEntityList.add(oe);
        }
        return ResponseResult.getSuccessResult(ordersEntityList);
    }

    @Override
    public ResponseResult<List<Map<String, Object>>> selectOrderByType() {
        return ResponseResult.getSuccessResult(ordersRepository.selectOrderByType());
    }

    @Override
    @Transactional
    public ResponseResult<OrdersEntity> submitOrder(OrdersEntity ordersEntity) {
        ordersEntity.setStatus(0);
        ordersEntity.setOrderdate(new Date());
        ordersRepository.save(ordersEntity);

        List<Integer> bgid = ordersEntity.getBgid();
        List<Integer> bshoppingnum = ordersEntity.getBshoppingnum();
        List<OrderdetailEntity> bods = new ArrayList<>();
        List<GoodsEntity> ges = new ArrayList<>();
        for (int i = 0; i < bgid.size(); i++) {
            OrderdetailEntity ot = new OrderdetailEntity();
            GoodsEntity ge = new GoodsEntity();
            ot.setOrderbasetableId(ordersEntity.getId());
            ot.setGoodstableId(bgid.get(i));
            ot.setShoppingnum(bshoppingnum.get(i));
            bods.add(ot);
            ge.setId(bgid.get(i));
            ge.setGstore(goodsService.getById(bgid.get(i)).getGstore() - bshoppingnum.get(i));
            ges.add(ge);
        }
        orderdetailService.saveBatch(bods);
        goodsService.updateBatchById(ges);

        CartEntity cartEntity = new CartEntity();
        cartEntity.setBusertableId(ordersEntity.getBusertableId());
        cartService.clearCart(cartEntity);
        return ResponseResult.getSuccessResult(ordersEntity);
    }

    @Override
    @Transactional
    public ResponseResult<OrdersEntity> goSubmitOrder(OrdersEntity ordersEntity) {
        ordersEntity.setStatus(0);
        ordersEntity.setOrderdate(new Date());
        ordersRepository.save(ordersEntity);

        List<Integer> bgid = ordersEntity.getBgid();
        List<Integer> bshoppingnum = ordersEntity.getBshoppingnum();
        List<OrderdetailEntity> bods = new ArrayList<>();
        List<GoodsEntity> ges = new ArrayList<>();
        for (int i = 0; i < bgid.size(); i++) {
            OrderdetailEntity ot = new OrderdetailEntity();
            GoodsEntity ge = new GoodsEntity();
            ot.setOrderbasetableId(ordersEntity.getId());
            ot.setGoodstableId(bgid.get(i));
            ot.setShoppingnum(bshoppingnum.get(i));
            bods.add(ot);
            ge.setId(bgid.get(i));
            ge.setGstore(goodsService.getById(bgid.get(i)).getGstore() - bshoppingnum.get(i));
            ges.add(ge);
        }
        orderdetailService.saveBatch(bods);
        goodsService.updateBatchById(ges);
        return ResponseResult.getSuccessResult(ordersEntity);
    }
}
