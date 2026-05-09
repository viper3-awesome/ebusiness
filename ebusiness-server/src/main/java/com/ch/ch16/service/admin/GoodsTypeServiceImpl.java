package com.ch.ch16.service.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.GoodsTypeEntity;
import com.ch.ch16.repository.admin.GoodsTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class GoodsTypeServiceImpl implements GoodsTypeService {
    @Autowired
    private GoodsTypeRepository goodsTypeRepository;
    @Autowired
    private GoodsService goodsService;

    @Override
    public ResponseResult<Map<String, Object>> getGoodsType(GoodsTypeEntity goodsTypeEntity) {
        int pageSize = goodsTypeEntity.getPageSize() != null ? goodsTypeEntity.getPageSize() : 5;
        int currentPage = goodsTypeEntity.getCurrentPage() != null ? goodsTypeEntity.getCurrentPage() : 1;
        PageRequest pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        var page = goodsTypeRepository.findAll(pageable);
        Map<String, Object> myres = new HashMap<>();
        myres.put("allGoodsTypes", page.getContent());
        myres.put("totalPage", page.getTotalPages());
        return ResponseResult.getSuccessResult(myres);
    }

    @Override
    public ResponseResult<Map<String, Object>> add(GoodsTypeEntity goodsTypeEntity) {
        boolean b = goodsTypeRepository.save(goodsTypeEntity) != null;
        if (b)
            return ResponseResult.getMessageResult(null, "A001");
        return ResponseResult.getMessageResult(null, "A002");
    }

    @Override
    public ResponseResult<Map<String, Object>> update(GoodsTypeEntity goodsTypeEntity) {
        boolean b = goodsTypeRepository.save(goodsTypeEntity) != null;
        if (b)
            return ResponseResult.getMessageResult(null, "A001");
        return ResponseResult.getMessageResult(null, "A002");
    }

    @Override
    public ResponseResult<Map<String, Object>> delete(GoodsTypeEntity goodsTypeEntity) {
        long res = goodsService.countByGoodsTypeId(goodsTypeEntity.getId());
        if (res > 0)
            return ResponseResult.getMessageResult(null, "A001");
        goodsTypeRepository.deleteById(goodsTypeEntity.getId());
        return ResponseResult.getMessageResult(null, "A002");
    }

    @Override
    public List<GoodsTypeEntity> list() {
        return goodsTypeRepository.findAll();
    }
}
