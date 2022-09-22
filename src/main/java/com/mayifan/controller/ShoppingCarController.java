package com.mayifan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mayifan.common.BaseContext;
import com.mayifan.common.R;
import com.mayifan.pojo.ShoppingCart;
import com.mayifan.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCarController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<String> addDishAndSetMeal(@RequestBody ShoppingCart shoppingCart){
        shoppingCartService.addDishOrSetMeal(shoppingCart);
        return R.success("添加成功！");
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        List<ShoppingCart> list_shoppingCart = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
        return R.success(list_shoppingCart);
    }

    //减少数量
    @PostMapping("/sub")
    public R<String> subDishOrSetMeal(@RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.subDishOrSetMeal(shoppingCart);
        return R.success("购物车删除成功！");
    }
    //清空购物车
    @DeleteMapping("/clean")
    public R<String> cleanShoppingCar(){
        shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId,BaseContext.getCurrentId()));
        return R.success("清除购物车成功");
    }

}
