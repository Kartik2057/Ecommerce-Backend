package com.kartik.Ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<CartItem>();

    private int totalPrice;
    private int totalItem;
    private int totalDiscountedPrice;
    private int discount;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cart{id=" + id + ", user=" + (user != null ? user.getId() : null) + ", cartItems=" + cartItems + ", totalPrice=" + totalPrice + "}";
    }
}
