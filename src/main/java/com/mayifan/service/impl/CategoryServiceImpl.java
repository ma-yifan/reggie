package com.mayifan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.pojo.Category;
import com.mayifan.service.CategoryService;
import com.mayifan.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author Mark
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2022-09-16 15:33:43
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




