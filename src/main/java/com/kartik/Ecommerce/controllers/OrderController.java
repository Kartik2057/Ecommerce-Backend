package com.kartik.Ecommerce.controllers;

import com.kartik.Ecommerce.exception.OrderException;
import com.kartik.Ecommerce.exception.UserException;
import com.kartik.Ecommerce.model.Address;
import com.kartik.Ecommerce.model.Order;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.services.OrderService;
import com.kartik.Ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;


    @PostMapping("")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress,
                                             @RequestHeader("Authorization")String jwt)throws UserException{
        User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.createOrder(user,shippingAddress);

        System.out.println("order "+order);
        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistory(
            @RequestHeader("Authorization")String jwt
    )throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<>(orders,HttpStatus.CREATED);
    }


    @GetMapping("/{Id}")
    public ResponseEntity<Order> findOrderById(
            @PathVariable("Id")Long orderId,
            @RequestHeader("Authorization")String jwt
    )throws UserException, OrderException{
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

}
