package com.mayifan.dto;

import com.mayifan.pojo.OrderDetail;
import com.mayifan.pojo.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;

}
