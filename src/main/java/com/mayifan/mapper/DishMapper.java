package com.mayifan.mapper;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayifan.dto.DishDto;
import org.apache.ibatis.annotations.Param;

import com.mayifan.pojo.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Mark
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2022-09-16 15:33:43
* @Entity com.mayifan.pojo.Dish
*/
public interface DishMapper extends BaseMapper<Dish> {

    DishDto selectAllById(@Param("id") Long id);

    Page<DishDto> selectDishDto(Page<DishDto> page,@Param("dishName") String dishName);

}




