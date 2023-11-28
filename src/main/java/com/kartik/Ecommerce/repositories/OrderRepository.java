package com.kartik.Ecommerce.repositories;


import com.kartik.Ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT o FROM Order o " +
            "WHERE o.user.id = :userId ")
    List<Order> getUserOrders(@Param("userId")Long userId);
//    "SELECT o FROM Order o " +
//            "WHERE o.user.id = :userId " +
//            "AND ( o.orderStatus = com.kartik.Ecommerce.model.OrderStatus.ORDER_PLACED " +
//            "OR o.orderStatus = com.kartik.Ecommerce.model.OrderStatus.ORDER_CONFIRMED " +
//            "OR o.orderStatus = com.kartik.Ecommerce.model.OrderStatus.ORDER_SHIPPED " +
//            "OR o.orderStatus = com.kartik.Ecommerce.model.OrderStatus.ORDER_DELIVERED)")
}
