package com.mayifan.service;

import com.mayifan.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Mark
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2022-09-21 20:07:10
*/
public interface OrdersService extends IService<Orders> {

    void submitAndPay(Orders orders_submit);
}
