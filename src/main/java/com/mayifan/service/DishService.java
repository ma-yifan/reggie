package com.mayifan.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayifan.dto.DishDto;
import com.mayifan.pojo.Category;
import com.mayifan.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Mark
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2022-09-16 15:33:43
*/
public interface DishService extends IService<Dish> {
    void saveDishAndDishFavor(DishDto dish_dto);

    DishDto selectDishDto(long dishId);

    void updateDishDto(DishDto dishDto);

    Page<DishDto> page_DishDto(Page<DishDto> page, String dishName);

    List<DishDto> listDishDto(Long category_id);
}
