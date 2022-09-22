package com.mayifan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayifan.common.R;
import com.mayifan.dto.DishDto;
import com.mayifan.dto.SetmealDto;
import com.mayifan.pojo.Setmeal;
import com.mayifan.service.SetmealService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Resource
    private SetmealService setmealService;

    @GetMapping("/page")
    public R<Page<SetmealDto>> selectAllMeal(long page,long pageSize,String name){
        Page<SetmealDto> page_setMealDto = setmealService.selectAllMealWithCategoryName(page, pageSize, name);
        return R.success(page_setMealDto);
    }

    @PostMapping
    public R<String> saveSetMeal(@RequestBody SetmealDto setmealDto) {
        setmealService.saveSetMeal(setmealDto);
        return R.success("套餐添加成功");
    }

    @GetMapping("/{setmealId}")
    public R<SetmealDto> querySetmealById(@PathVariable("setmealId") Long setmealId){
        SetmealDto setmealDto = setmealService.querySetmealById(setmealId);
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmeal(setmealDto);
        return R.success("修改成功");
    }

    /*
    状态修改，停售或者起售
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") int status, @RequestParam("ids") List<Long> ids) {
        List<Setmeal> list_setmeal = ids.stream().map(item -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(item);
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(list_setmeal);
        return R.success("状态修改成功");
    }

    /*
    删除
     */
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam("ids") List<Long> ids) {
        setmealService.removeByIds(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> listByCategoryId(@RequestParam("categoryId") Long id){
        List<Setmeal> list_setmeal = setmealService.list(new LambdaQueryWrapper<Setmeal>()
                .eq(id != null, Setmeal::getCategoryId, id));
        return R.success(list_setmeal);

    }

    @GetMapping("/dish/{setMealId}")
    public R<List<DishDto>> querySetMealDtoBy(@PathVariable("setMealId") Long id){
        List<DishDto> dishDtos = setmealService.queryDishDtoes(id);
        return R.success(dishDtos);
    }
}
