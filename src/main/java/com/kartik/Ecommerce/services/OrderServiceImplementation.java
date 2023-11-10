package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.OrderException;
import com.kartik.Ecommerce.model.Address;
import com.kartik.Ecommerce.model.Order;
import com.kartik.Ecommerce.model.OrderStatus;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.repositories.CartRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceImplementation implements OrderService{

    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;
    @Override
    public Order createOrder(User user, Address shippingAddress) {
        return null;
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return null;
    }

    @Override
    public Order changeOrderStatus(Long orderId, OrderStatus orderStatus) throws OrderException {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {

    }

    @Override
    public Order findOrderById(Long orderId) {
        return new Order();
    }
}
