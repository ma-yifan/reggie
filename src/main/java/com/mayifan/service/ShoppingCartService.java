package com.mayifan.service;

import com.mayifan.pojo.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Mark
* @description 针对表【shopping_cart(购物车)】的数据库操作Service
* @createDate 2022-09-20 20:08:26
*/
public interface ShoppingCartService extends IService<ShoppingCart> {

    void addDishOrSetMeal(ShoppingCart shoppingCart);

    void subDishOrSetMeal(ShoppingCart shoppingCart);
}

