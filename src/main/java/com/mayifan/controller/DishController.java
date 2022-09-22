package com.mayifan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayifan.common.R;
import com.mayifan.dto.DishDto;
import com.mayifan.pojo.Dish;
import com.mayifan.pojo.DishFlavor;
import com.mayifan.service.DishFlavorService;
import com.mayifan.service.DishService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private DishService dishService;

    @Resource
    private DishFlavorService dishFlavorService;

    @GetMapping("/page")
    public R<Page<DishDto>> getDishPage(@RequestParam("page") long current,@RequestParam("pageSize") long pageSize,
                                     @RequestParam(value = "name",required = false) String dish_name){
        Page<DishDto> page_dishDto = dishService.page_DishDto(new Page<DishDto>(current, pageSize), dish_name);
        return R.success(page_dishDto);
    }

    /*
    添加菜品
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dish_dto){
        dishService.saveDishAndDishFavor(dish_dto);
        return R.success("添加菜品成功");
    }
    /*
    更新(单个和批量)
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status,@RequestParam("ids") List<Long> ids){
        ArrayList<Dish> list_dish = new ArrayList<>();
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setStatus(status);
            dish.setId(id);
            list_dish.add(dish);
        }
        dishService.updateBatchById(list_dish);
        return R.success("删除成功");
    }
    /*
    删除（批量，单个）
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> ids){
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }

    /*
    查找单个dish
     */
    @GetMapping("/{id}")
    public R<DishDto> queryDishById(@PathVariable Long id){
        DishDto dishDto = dishService.selectDishDto(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dish_dto){
        dishService.updateDishDto(dish_dto);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> selectDishByCategoryId(@RequestParam("categoryId") Long category_id){
//        List<Dish> list_dish = dishService.list(new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, category_id));
        List<DishDto> list_DishDto = dishService.listDishDto(category_id);
        return R.success(list_DishDto);
    }


}
