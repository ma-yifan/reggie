package com.mayifan.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.mayifan.pojo.Dish;
import com.mayifan.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Dish的DTO对象（Data Transfer Object）
 */
@Data
public class DishDto extends Dish {


    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
