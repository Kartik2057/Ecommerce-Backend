package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.OrderException;
import com.kartik.Ecommerce.model.*;
import com.kartik.Ecommerce.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceImplementation implements OrderService{

    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;
    private AddressRepository addressRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private UserRepository userRepository;
    private CartService cartService;
    @Override
    public Order createOrder(User user, Address shippingAddress) {
        shippingAddress.setUser(user);
        Address address = addressRepository.save(shippingAddress);
        user.getAddresses().add(address);
        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getId());
        //New orderItems List for adding to the order
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem:cart.getCartItems()){
            //Creating new orderItem for adding to the orderItems list
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSize(cartItem.getSize());
            orderItem.setUserId(cartItem.getUserId());
            orderItem.setDiscountedPrice(cartItem.getDiscountedPrice());


            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }


        Order createdOrder = new Order();
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscount());
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus(OrderStatus.ORDER_PLACED);
        createdOrder.getPaymentDetails().setStatus("PENDING");
        createdOrder.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(createdOrder);


        //Updating savedOrder information in all the orderItems
        for(OrderItem item:orderItems){
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        List<Order> orders = orderRepository.getUserOrders(userId);
        return orders;
    }

    @Override
    public Order changeOrderStatus(Long orderId, OrderStatus orderStatus) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        order.getPaymentDetails().setStatus("COMPLETED");
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        orderRepository.deleteById(orderId);
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException{
        Optional<Order> opt = orderRepository.findById(orderId);

        if(opt.isPresent()){
            return opt.get();
        }
        throw new OrderException("order not exist with id"+orderId);
    }
}
