package com.mayifan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayifan.common.R;
import com.mayifan.exception.CustomException;
import com.mayifan.pojo.Category;
import com.mayifan.pojo.Dish;
import com.mayifan.pojo.Setmeal;
import com.mayifan.service.CategoryService;
import com.mayifan.service.DishService;
import com.mayifan.service.SetmealService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    @GetMapping("/page")
    public R<Page<Category>> selectPage(@RequestParam("page") long current, @RequestParam("pageSize") long pageSize) {
        Page<Category> page = categoryService.page(new Page<Category>(current, pageSize),
                new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort));
        return R.success(page);
    }

    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加类品成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改菜品成功");
    }
    /*
    品类一般被各个菜或者套餐所依赖，所以一般情况下，我们删之前都要查询是否有被其他表依赖，再进行删除，
    不要使用外键，外键对性能的消耗很大
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") long id){
        Category select_category = categoryService.getById(id);
        if (select_category.getType()==1) {
            //菜品分类
            LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, id);
            List<Dish> dish_list = dishService.list(wrapper);
            if (!dish_list.isEmpty()) {
                throw new CustomException("当前品类还在售哦，请勿删除！");
            }
        }else if(select_category.getType()==2) {
            //套餐分类
            LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId, id);
            List<Setmeal> meal_list = setmealService.list(wrapper);
            if (!meal_list.isEmpty()) {
                throw new CustomException("当前套餐还在售哦，请勿删除！");
            }
        }
        categoryService.removeById(id);
        return R.success("品类删除成功");
    }

    @GetMapping("/list")
    public R<List<Category>> selectCategoryByType(@RequestParam(value = "type",required = false) Integer type){
        List<Category> categoryList = categoryService.list(new LambdaQueryWrapper<Category>().eq(type != null,
                Category::getType, type));
        return R.success(categoryList);
    }
}
