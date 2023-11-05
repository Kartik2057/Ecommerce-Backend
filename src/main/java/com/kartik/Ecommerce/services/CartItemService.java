package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.CartItemException;
import com.kartik.Ecommerce.exception.UserException;
import com.kartik.Ecommerce.model.Cart;
import com.kartik.Ecommerce.model.CartItem;
import com.kartik.Ecommerce.model.Product;

public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem)throws CartItemException, UserException;
    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId);

    public void removeCartItem(Long userId, Long cartItemId)throws CartItemException,UserException;

    public CartItem findCartItemById(Long cartItemId)throws CartItemException;

}
