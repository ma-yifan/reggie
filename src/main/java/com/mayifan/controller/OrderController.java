package com.mayifan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayifan.common.R;
import com.mayifan.dto.OrderDto;
import com.mayifan.pojo.OrderDetail;
import com.mayifan.pojo.Orders;
import com.mayifan.service.OrderDetailService;
import com.mayifan.service.OrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrdersService ordersService;

    @Resource
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submitAndPay(orders);
        return R.success("提交支付成功");
    }

    @GetMapping("/userPage")
    public R<Page<OrderDto>> firstOrder(long page,long pageSize) {
        Page<Orders> page_order = ordersService.page(new Page<Orders>(page, pageSize), new LambdaQueryWrapper<Orders>()
                .orderByDesc(Orders::getCheckoutTime));
        Page<OrderDto> page_orderDto = new Page<>();
        BeanUtils.copyProperties(page_order,page_orderDto,"orderDetails");
        List<Orders> orders = page_order.getRecords();
        List<OrderDto> list_orderDto = orders.stream().map(item -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);
            List<OrderDetail> list = orderDetailService.list(new LambdaQueryWrapper<OrderDetail>()
                    .eq(OrderDetail::getOrderId, item.getId()));
            orderDto.setOrderDetails(list);
            return orderDto;
        }).collect(Collectors.toList());
        page_orderDto.setRecords(list_orderDto);
        return R.success(page_orderDto);
    }


}
