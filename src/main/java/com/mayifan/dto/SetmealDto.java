package com.mayifan.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.mayifan.pojo.Dish;
import com.mayifan.pojo.Setmeal;
import com.mayifan.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {


    private String categoryName;

    private List<SetmealDish> setmealDishes;

}
