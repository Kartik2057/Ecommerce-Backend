package com.kartik.Ecommerce.exception;

import com.kartik.Ecommerce.services.CartItemService;

public class CartItemException extends Exception {
    public CartItemException(String message){
        super(message);
    }
}
