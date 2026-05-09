package com.ch.ch16.service.admin;

import com.ch.ch16.common.MyUtil;
import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.CartEntity;
import com.ch.ch16.entity.GoodsEntity;
import com.ch.ch16.entity.GoodsTypeEntity;
import com.ch.ch16.entity.OrderdetailEntity;
import com.ch.ch16.repository.admin.GoodsRepository;
import com.ch.ch16.repository.admin.GoodsTypeRepository;
import com.ch.ch16.service.before.CartService;
import com.ch.ch16.service.before.FocusService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@SuppressWarnings("all")
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsTypeRepository goodsTypeRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private FocusService focusService;
    @Autowired
    private OrderdetailService orderdetailService;

    @Override
    public ResponseResult<Map<String, Object>> getGoods(GoodsEntity goodsEntity) {
        int pageSize = goodsEntity.getPageSize() != null ? goodsEntity.getPageSize() : 5;
        int currentPage = goodsEntity.getCurrentPage() != null ? goodsEntity.getCurrentPage() : 1;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Specification<GoodsEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (goodsEntity.getGoodstypeId() != null && goodsEntity.getGoodstypeId() > 0)
                predicates.add(cb.equal(root.get("goodstypeId"), goodsEntity.getGoodstypeId()));
            if (goodsEntity.getGname() != null && goodsEntity.getGname().length() > 0)
                predicates.add(cb.like(root.get("gname"), "%" + goodsEntity.getGname() + "%"));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        var page = goodsRepository.findAll(spec, pageable);
        // 填充typename
        List<GoodsTypeEntity> types = goodsTypeRepository.findAll();
        Map<Integer, String> typeMap = new HashMap<>();
        for (GoodsTypeEntity t : types)
            typeMap.put(t.getId(), t.getTypename());
        for (GoodsEntity g : page.getContent())
            g.setTypename(typeMap.get(g.getGoodstypeId()));

        Map<String, Object> myres = new HashMap<>();
        myres.put("allGoods", page.getContent());
        myres.put("totalPage", page.getTotalPages());
        return ResponseResult.getSuccessResult(myres);
    }

    @Override
    public ResponseResult<Map<String, Object>> iPageMyFocusGoods(GoodsEntity goodsEntity) {
        int pageSize = goodsEntity.getPageSize() != null ? goodsEntity.getPageSize() : 5;
        int currentPage = goodsEntity.getCurrentPage() != null ? goodsEntity.getCurrentPage() : 1;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var page = goodsRepository.findMyFocusGoods(goodsEntity.getBusertableId(), pageable);
        // 填充typename
        List<GoodsTypeEntity> types = goodsTypeRepository.findAll();
        Map<Integer, String> typeMap = new HashMap<>();
        for (GoodsTypeEntity t : types)
            typeMap.put(t.getId(), t.getTypename());
        for (GoodsEntity g : page.getContent())
            g.setTypename(typeMap.get(g.getGoodstypeId()));

        Map<String, Object> myres = new HashMap<>();
        myres.put("allGoods", page.getContent());
        myres.put("totalPage", page.getTotalPages());
        return ResponseResult.getSuccessResult(myres);
    }

    @Override
    public ResponseResult<List<Map<String, Object>>> myCartGoods(CartEntity cartEntity) {
        List<Map<String, Object>> cartGoods = goodsRepository.myCartGoods(cartEntity.getBusertableId());
        return ResponseResult.getSuccessResult(cartGoods);
    }

    @Override
    public ResponseResult<List<GoodsEntity>> getAdvGoods() {
        Specification<GoodsEntity> spec = (root, query, cb) ->
                cb.equal(root.get("isAdvertisement"), 1);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        List<GoodsEntity> listAdv = goodsRepository.findAll(spec, pageable).getContent();
        return ResponseResult.getSuccessResult(listAdv);
    }

    @Override
    public ResponseResult<Map<String, String>> add(GoodsEntity goodsEntity) {
        byte[] myfile = goodsEntity.getLogoFile();
        if (myfile != null && myfile.length > 0) {
            String path = "D:\\VS-workspace\\ebusiness-vue\\src\\assets";
            String fileName = goodsEntity.getFileName();
            String fileNewName = MyUtil.getNewFileName(fileName);
            File filePath = new File(path + File.separator + fileNewName);
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                out.write(myfile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            goodsEntity.setGpicture(fileNewName);
        }
        if ("add".equals(goodsEntity.getAct())) {
            goodsRepository.save(goodsEntity);
            return ResponseResult.getMessageResult(null, "A001");
        } else {
            goodsRepository.save(goodsEntity);
            return ResponseResult.getMessageResult(null, "A001");
        }
    }

    @Override
    public ResponseResult<Map<String, String>> delete(GoodsEntity goodsEntity) {
        long res1 = cartService.countByGoodsId(goodsEntity.getId());
        long res2 = focusService.countByGoodsId(goodsEntity.getId());
        OrderdetailEntity oe = new OrderdetailEntity();
        oe.setGoodstableId(goodsEntity.getId());
        long res3 = orderdetailService.count(oe);
        if (res1 > 0 || res2 > 0 || res3 > 0)
            return ResponseResult.getMessageResult(null, "A001");
        goodsRepository.deleteById(goodsEntity.getId());
        return ResponseResult.getMessageResult(null, "A002");
    }

    @Override
    public ResponseResult<List<GoodsEntity>> getGoodsIndex(GoodsEntity goodsEntity) {
        Specification<GoodsEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (goodsEntity.getGoodstypeId() != null && goodsEntity.getGoodstypeId() != 0)
                predicates.add(cb.equal(root.get("goodstypeId"), goodsEntity.getGoodstypeId()));
            if (goodsEntity.getGname() != null && goodsEntity.getGname().trim().length() > 0)
                predicates.add(cb.like(root.get("gname"), "%" + goodsEntity.getGname() + "%"));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "id"));
        List<GoodsEntity> listIndex = goodsRepository.findAll(spec, pageable).getContent();
        return ResponseResult.getSuccessResult(listIndex);
    }

    @Override
    public ResponseResult<GoodsEntity> getGoodsById(GoodsEntity goodsEntity) {
        return ResponseResult.getSuccessResult(goodsRepository.findById(goodsEntity.getId()).orElse(null));
    }

    @Override
    public long countByGoodsTypeId(Integer goodsTypeId) {
        Specification<GoodsEntity> spec = (root, query, cb) ->
                cb.equal(root.get("goodstypeId"), goodsTypeId);
        return goodsRepository.count(spec);
    }

    @Override
    public GoodsEntity getById(Integer id) {
        return goodsRepository.findById(id).orElse(null);
    }

    @Override
    public void updateById(GoodsEntity entity) {
        goodsRepository.save(entity);
    }

    @Override
    public void updateBatchById(List<GoodsEntity> entities) {
        goodsRepository.saveAll(entities);
    }
}
