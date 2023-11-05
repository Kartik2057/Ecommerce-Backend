package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.OrderException;
import com.kartik.Ecommerce.model.Address;
import com.kartik.Ecommerce.model.Order;
import com.kartik.Ecommerce.model.OrderStatus;
import com.kartik.Ecommerce.model.User;

import java.util.List;

public interface OrderService {
    public Order createOrder(User user, Address shippingAddress);
    public List<Order> usersOrderHistory(Long userId);
    public Order changeOrderStatus(Long orderId, OrderStatus orderStatus)throws OrderException;
    public List<Order> getAllOrders();
    public void deleteOrder(Long orderId)throws OrderException;
}
