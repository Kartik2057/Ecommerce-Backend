package com.kartik.Ecommerce.controllers;

import com.kartik.Ecommerce.exception.OrderException;
import com.kartik.Ecommerce.model.Order;
import com.kartik.Ecommerce.model.OrderStatus;
import com.kartik.Ecommerce.response.ApiResponse;
import com.kartik.Ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<List<Order>>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> confirmOrder(@PathVariable Long orderId,
                                              @RequestHeader("Authorization")String jwt) throws OrderException {
        Order order = orderService.changeOrderStatus(orderId, OrderStatus.ORDER_CONFIRMED);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
    @PutMapping("/{orderId}/shipped")
    public ResponseEntity<Order> shippedOrder(@PathVariable Long orderId,
                                              @RequestHeader("Authorization")String jwt) throws OrderException {
        Order order = orderService.changeOrderStatus(orderId,OrderStatus.ORDER_SHIPPED);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
    @PutMapping("/{orderId}/delivered")
    public ResponseEntity<Order> deliveredOrder(@PathVariable Long orderId,
                                              @RequestHeader("Authorization")String jwt) throws OrderException {
        Order order = orderService.changeOrderStatus(orderId,OrderStatus.ORDER_DELIVERED);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
    @PutMapping("/{orderId}/cancelled")
    public ResponseEntity<ApiResponse> cancelledOrder(@PathVariable Long orderId,
                                              @RequestHeader("Authorization")String jwt) throws OrderException {
        orderService.deleteOrder(orderId);

        ApiResponse res = new ApiResponse();
        res.setMessage("order deleted successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res,HttpStatus.OK);
    }


}
