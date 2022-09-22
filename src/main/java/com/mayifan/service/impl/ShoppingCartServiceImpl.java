package com.mayifan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.common.R;
import com.mayifan.pojo.ShoppingCart;
import com.mayifan.service.ShoppingCartService;
import com.mayifan.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author Mark
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2022-09-20 20:08:26
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

    @Transactional
    @Override
    public void addDishOrSetMeal(ShoppingCart shoppingCart) {
        //先判断购物车有没有这个菜品或者套餐,有的话直接在数量和金额上累加
        ShoppingCart select_sc = this.getOne(new LambdaQueryWrapper<ShoppingCart>()
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId()));
        if (select_sc != null) {
            select_sc.setAmount(select_sc.getAmount().add(shoppingCart.getAmount()));
            select_sc.setNumber(select_sc.getNumber()+1);
            this.updateById(select_sc);
            return;
        }
        //没有直接进行添加
        this.save(shoppingCart);
    }
    @Transactional
    @Override
    public void subDishOrSetMeal(ShoppingCart shoppingCart) {
        ShoppingCart select_sc = this.getOne(new LambdaQueryWrapper<ShoppingCart>()
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId()));
        int count = select_sc.getNumber() - 1;
        if (count>0) {
            select_sc.setNumber(count);
            this.updateById(select_sc);
            return;
        }
        this.remove(new LambdaQueryWrapper<ShoppingCart>()
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId()));
    }
}




