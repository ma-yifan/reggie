package com.mayifan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.dto.DishDto;
import com.mayifan.pojo.Category;
import com.mayifan.pojo.Dish;
import com.mayifan.pojo.DishFlavor;
import com.mayifan.service.CategoryService;
import com.mayifan.service.DishFlavorService;
import com.mayifan.service.DishService;
import com.mayifan.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Mark
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2022-09-16 15:33:43
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{

    @Resource
    private DishMapper dishMapper;

    @Resource
    private CategoryService categoryService;

    @Resource
    private DishFlavorService dishFlavorService;
    /*
    dish和dish_favor是放在两张表上，需要我们去操作两张表，所以需要在service进行操作，方便我们进行事务控制
     */
    @Transactional
    @Override
    public void saveDishAndDishFavor(DishDto dish_dto) {
        this.save(dish_dto);
        //dishFavor对象传过来的时候是缺少dishId的，需要我们自己进预处理
        List<DishFlavor> list_favor = dish_dto.getFlavors();
        list_favor = list_favor.stream().map(item -> {
            item.setDishId(dish_dto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(list_favor);
    }
    @Transactional
    @Override
    public DishDto selectDishDto(long dishId) {
        DishDto dishDto = dishMapper.selectAllById(dishId);
        List<DishFlavor> list_dishFavor = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>()
                .eq(DishFlavor::getDishId, dishDto.getId()));
        dishDto.setFlavors(list_dishFavor);
        Category category = categoryService.getById(dishDto.getCategoryId());
        dishDto.setCategoryName(category.getName());
        return dishDto;
    }

    @Transactional
    @Override
    public void updateDishDto(DishDto dishDto) {
        //先更新dish表
        this.updateById(dishDto);
        //更新口味dishFlavor
        //删除旧口味
        dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId,dishDto.getId()));
        //新的口味
        List<DishFlavor> list_DishFlavor = dishDto.getFlavors();
        list_DishFlavor = list_DishFlavor.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(list_DishFlavor);
    }


    @Override
    public Page<DishDto> page_DishDto(Page<DishDto> page, String dishName) {
        Page<DishDto> page_DishDto = dishMapper.selectDishDto(page, dishName);
        return page_DishDto;
    }

    @Override
    public List<DishDto> listDishDto(Long category_id) {
        List<Dish> list_dish = this.list(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getCategoryId, category_id));
        return list_dish.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            List<DishFlavor> list_flavor = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>()
                    .eq(DishFlavor::getDishId, item.getId()));
            dishDto.setFlavors(list_flavor);
            return dishDto;
        }).collect(Collectors.toList());
    }

}




