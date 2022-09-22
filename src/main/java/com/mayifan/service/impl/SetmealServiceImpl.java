package com.mayifan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.common.R;
import com.mayifan.dto.DishDto;
import com.mayifan.dto.SetmealDto;
import com.mayifan.pojo.Category;
import com.mayifan.pojo.Dish;
import com.mayifan.pojo.Setmeal;
import com.mayifan.pojo.SetmealDish;
import com.mayifan.service.CategoryService;
import com.mayifan.service.DishService;
import com.mayifan.service.SetmealDishService;
import com.mayifan.service.SetmealService;
import com.mayifan.mapper.SetmealMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Mark
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2022-09-16 15:33:43
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService{

    @Resource
    private CategoryService categoryService;

    @Resource
    private SetmealDishService setmealDishService;

    @Resource
    private DishService dishService;



    /*
    DTO对象一般是多个的表的字段组成，如果想要将信息封装成一个DTO对象需要进行多表查询，MP无所一次性对多表进行查询，
    可以采用表关联进行查询，或者分表进行查询的方法
    以下就是分表查询的办法
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public Page<SetmealDto> selectAllMealWithCategoryName(long page,long pageSize,String name) {
        //找出父类继承的信息
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>()
                .like(StringUtils.hasLength(name), Setmeal::getName, name);
        Page<Setmeal> page_setmeal = this.page(new Page<Setmeal>(), wrapper);
        Page<SetmealDto> page_SetmealDto = new Page<>();
        //使用BeanUtils先将部分信息进行复制
        BeanUtils.copyProperties(page_setmeal,page_SetmealDto,"records");
        //对于缺少的部分，先通过遍历父类元素，找到每一元素缺少的字段值，再转成新的对象
        List<Setmeal> list_setmeal = page_setmeal.getRecords();
        List<SetmealDto> list_setMealDto = null;
        list_setMealDto = list_setmeal.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        page_SetmealDto.setRecords(list_setMealDto);

        return page_SetmealDto;
    }

    @Transactional
    @Override
    public void saveSetMeal(SetmealDto setmealDto) {
        //先添加套餐
        this.save(setmealDto);
        //将套餐中的菜品添加到setmeal_dish表中
        List<SetmealDish> list_SetMealDishs = setmealDto.getSetmealDishes();
        list_SetMealDishs = list_SetMealDishs.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(list_SetMealDishs);
    }

    @Override
    public SetmealDto querySetmealById(Long setmealId) {
        //基本的数据
        Setmeal setmeal = this.getById(setmealId);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        //分类名
        setmealDto.setCategoryName(categoryService.getById(setmeal.getCategoryId()).getName());
        //套餐菜品
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> list_setmealDish = setmealDishService.list(wrapper);
        setmealDto.setSetmealDishes(list_setmealDish);
        return setmealDto;
    }

    @Override
    public void updateSetmeal(SetmealDto setmealDto) {
        //基本的数据先更新
        this.updateById(setmealDto);
        //先删除再插入（更新）
        setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId,setmealDto.getId()));
        List<SetmealDish> list_SetmealDish = setmealDto.getSetmealDishes();
        list_SetmealDish = list_SetmealDish.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(list_SetmealDish);
        //
    }

    @Override
    public List<DishDto> queryDishDtoes(Long id) {
        List<SetmealDish> list_setmealDish = setmealDishService.list(new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId, id)
                .select(SetmealDish::getDishId,SetmealDish::getCopies));
         return list_setmealDish.stream().map(item -> {
            DishDto dishDto = new DishDto();
            Dish select_dish = dishService.getById(item.getDishId());
            BeanUtils.copyProperties(select_dish, dishDto);
            dishDto.setCopies(item.getCopies());
            return dishDto;
        }).collect(Collectors.toList());

    }
}




