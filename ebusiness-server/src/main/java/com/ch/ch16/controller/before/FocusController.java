package com.ch.ch16.controller.before;

import com.ch.ch16.common.http.ResponseResult;
import com.ch.ch16.entity.FocusEntity;
import com.ch.ch16.entity.GoodsEntity;
import com.ch.ch16.service.admin.GoodsService;
import com.ch.ch16.service.before.FocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/before/focus")
@SuppressWarnings("all")
public class FocusController {
    @Autowired
    private FocusService focusService;
    @Autowired
    private GoodsService goodsService;
    @PostMapping("/add")
    public ResponseResult<Map<String, Object>> add(@RequestBody FocusEntity focusEntity){
        return focusService.focus(focusEntity);
    }
    //查询我的收藏
    @PostMapping("/getMyFocusGoods")
    public ResponseResult<Map<String, Object>> getMyFocusGoods(@RequestBody GoodsEntity goodsEntity){
        return goodsService.iPageMyFocusGoods(goodsEntity);
    }
}
