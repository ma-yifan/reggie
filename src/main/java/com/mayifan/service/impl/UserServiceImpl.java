package com.mayifan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.pojo.User;
import com.mayifan.service.UserService;
import com.mayifan.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Mark
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-09-16 15:33:43
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




