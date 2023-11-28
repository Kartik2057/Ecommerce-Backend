package com.kartik.Ecommerce.controllers;

import com.kartik.Ecommerce.exception.CartItemException;
import com.kartik.Ecommerce.exception.UserException;
import com.kartik.Ecommerce.model.CartItem;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.requests.AddItemRequest;
import com.kartik.Ecommerce.requests.UpdateCartItemRequest;
import com.kartik.Ecommerce.response.ApiResponse;
import com.kartik.Ecommerce.services.CartItemService;
import com.kartik.Ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt
    )
            throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res = new ApiResponse();
        res.setMessage("Item deleted Successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItem(
            @RequestBody UpdateCartItemRequest req,
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization")String jwt
    )throws UserException,CartItemException{
        User user = userService.findUserProfileByJwt(jwt);
        CartItem newCartItem = new CartItem();
        newCartItem.setQuantity(req.getQuantity());
        cartItemService.updateCartItem(user.getId(),cartItemId,newCartItem);
//        System.out.println("Inside update function");
        ApiResponse res = new ApiResponse();
        res.setMessage("Item updated Successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
