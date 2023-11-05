package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.model.Cart;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.requests.AddItemRequest;


public interface CartService {
    public Cart createCart(User user);
    public String addCartItem(Long userId, AddItemRequest request)throws ProductException;

    public Cart findUserCart(Long userId);
}
