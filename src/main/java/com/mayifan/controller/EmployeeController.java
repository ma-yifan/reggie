package com.mayifan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayifan.common.R;
import com.mayifan.pojo.Employee;
import com.mayifan.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee emp){
        //密码加密
        emp.setPassword(DigestUtils.md5DigestAsHex(emp.getPassword().getBytes(StandardCharsets.UTF_8)));
        //根据用户名查找数据库
        Employee login_emp = employeeService.getOne(new LambdaQueryWrapper<Employee>().eq(Employee::getUsername, emp.getUsername()));
        R<Employee> r = null;
        if (login_emp!=null) {
            //进行密码的比对
            if(login_emp.getPassword().equals(emp.getPassword())){
                //密码正确，查看用户的状态是否使用
                if (login_emp.getStatus()==1) {
                    r = R.success(login_emp);
                    request.getSession().setAttribute("employee",login_emp.getId());//
                }
                else{
                    //用户状态不可用
                    r = R.error("当前账户已被禁用");
                }
            }else{
                //密码错误
                r = R.error("用户名或者密码错误");
            }
        }else {
            r = R.error("该用户不存在，请进行登录注册");
        }
        //响应数据
        return r;
    }

    @PostMapping("/logout")
    public R<Employee> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success(null);
    }

    @PostMapping
    public R<String> addEmployee(@RequestBody Employee add_emp){
        /*
        账户做了唯一性处理，我们直接添加，是有可能出现报错的情况，
        如果我们先进行查询，会给数据库造成很大压力
         */
        /*Employee select_emp = employeeService.getOne(new LambdaQueryWrapper<Employee>().eq(Employee::getUsername, add_emp.getUsername()));
        R<String> r = null;
        if (select_emp!=null) {
            r = R.error("该员工账户已经存在");
        }

        boolean flag = employeeService.save(add_emp);
        if (!flag) {
            r = R.error("添加失败，请重新操作");
        }else{
            r = R.success(null);
            System.out.println(add_emp);
        }
        return r;*/
        //如果没有添加成功，怎么进行全局异常处理
        employeeService.save(add_emp);
        return R.success("添加成功");


    }

    @GetMapping("/page")
    public R<Page<Employee>> pageEmp(@RequestParam("page") long current,@RequestParam("pageSize") long size,
                                     @RequestParam(value = "name",required = false) String nameVo) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .like(StringUtils.hasLength(nameVo),Employee::getName, nameVo);
        Page<Employee> page = employeeService.page(new Page<Employee>(current, size), wrapper);
        return R.success(page);
    }
    /*
    修改员工状态
    注：这里有个小问题，在对象转json的过程中，id经过雪花算法是19位，js的long类型转换最大是16位，转换过程中会造成id属性
    丢失精度，解决办法就是将对象中的long类型在转换过程中，转成String类型，就可以避免js在解析过程出现丢失精度的现象
     */
    @PutMapping
    public R<String> enableOrDisableEmployee(@RequestBody Employee update_emp){
        employeeService.updateById(update_emp);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> selectEmpById(@PathVariable("id") Long emp_id){
        Employee select_emp = employeeService.getById(emp_id);
        return R.success(select_emp);
    }
}
