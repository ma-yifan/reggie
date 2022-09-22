package com.mayifan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.pojo.DishFlavor;
import com.mayifan.service.DishFlavorService;
import com.mayifan.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author Mark
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-09-18 20:41:48
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




