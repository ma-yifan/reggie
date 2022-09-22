package com.mayifan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.pojo.OrderDetail;
import com.mayifan.service.OrderDetailService;
import com.mayifan.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author Mark
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-09-21 20:07:10
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




