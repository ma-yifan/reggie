package com.mayifan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayifan.common.BaseContext;
import com.mayifan.pojo.AddressBook;
import com.mayifan.pojo.OrderDetail;
import com.mayifan.pojo.Orders;
import com.mayifan.pojo.ShoppingCart;
import com.mayifan.service.AddressBookService;
import com.mayifan.service.OrderDetailService;
import com.mayifan.service.OrdersService;
import com.mayifan.mapper.OrdersMapper;
import com.mayifan.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Mark
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2022-09-21 20:07:10
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Resource
    private AddressBookService addressBookService;
    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private OrderDetailService orderDetailService;

    @Transactional
    @Override
    public void submitAndPay(Orders orders_submit) {
        //根据addressId,userId分别找到地址信息，以及购物车信息
        //地址信息
        AddressBook select_address = addressBookService.getById(orders_submit.getAddressBookId());
        orders_submit.setPhone(select_address.getPhone());
        orders_submit.setAddress(select_address.getDetail());
        orders_submit.setConsignee(select_address.getConsignee());
        //根据域中的user_id找到用户的购物车信息
        //订单信息,金额
        List<ShoppingCart> list_shoppingCart = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>().
                eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
        orders_submit.setAmount(new BigDecimal(0.0));
        list_shoppingCart.stream().forEach(item -> {
            BigDecimal add = orders_submit.getAmount().add(item.getAmount());
            orders_submit.setAmount(add);
        });
        //将所有的信息存入，并清空用户的购物车
        this.save(orders_submit);
        shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
        //遍历将购物车中的商品信息转成订单的具体信息
        List<OrderDetail> list_orderDetail = list_shoppingCart.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail);
            orderDetail.setOrderId(orders_submit.getId());
            return orderDetail;
        }).collect(Collectors.toList());
       //添加订单信息
        orderDetailService.saveBatch(list_orderDetail);
    }


}




