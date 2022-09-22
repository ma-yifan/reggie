package com.mayifan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayifan.dto.DishDto;
import com.mayifan.dto.SetmealDto;
import com.mayifan.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Mark
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-09-16 15:33:43
*/
public interface SetmealService extends IService<Setmeal> {

    Page<SetmealDto> selectAllMealWithCategoryName(long page,long pageSize,String name);


    void saveSetMeal(SetmealDto setmealDto);

    SetmealDto querySetmealById(Long setmealId);

    void updateSetmeal(SetmealDto setmealDto);

    List<DishDto> queryDishDtoes(Long id);
}
