package com.ch.ch16.controller.admin;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.common.sercurity.anno.AuthIgrone;
import com.ch.ch16.entity.GoodsTypeEntity;
import com.ch.ch16.service.admin.GoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/type")
@SuppressWarnings("all")
public class GoodsTypeController {
    @Autowired
    private GoodsTypeService goodsTypeService;
    @AuthIgrone //因为before的导航页需要查询商品类型
    @PostMapping("/getAllGoodsType")
    public ResponseResult<List<GoodsTypeEntity>> getAllGoodsType(){
        //{result:goodsTypeService.list()}
        return ResponseResult.getSuccessResult(goodsTypeService.list());
    }
    @PostMapping("/getGoodsType")
    public ResponseResult<Map<String, Object>> getGoodsType(@RequestBody GoodsTypeEntity goodsTypeEntity){
        return goodsTypeService.getGoodsType(goodsTypeEntity);
    }
    @PostMapping("/add")
    public ResponseResult<Map<String, Object>> add(@RequestBody GoodsTypeEntity goodsTypeEntity){
        return goodsTypeService.add(goodsTypeEntity);
    }
    @PostMapping("/update")
    public ResponseResult<Map<String, Object>> update(@RequestBody GoodsTypeEntity goodsTypeEntity){
        return goodsTypeService.update(goodsTypeEntity);
    }
    @PostMapping("/delete")
    public ResponseResult<Map<String, Object>> delete(@RequestBody GoodsTypeEntity goodsTypeEntity){
        return goodsTypeService.delete(goodsTypeEntity);
    }
}
