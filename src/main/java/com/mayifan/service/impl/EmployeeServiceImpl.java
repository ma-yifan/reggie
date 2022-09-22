package com.mayifan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.pojo.Employee;
import com.mayifan.service.EmployeeService;
import com.mayifan.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author Mark
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2022-09-16 15:33:43
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




