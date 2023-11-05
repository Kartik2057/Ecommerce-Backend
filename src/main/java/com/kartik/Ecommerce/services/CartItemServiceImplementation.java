package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.CartItemException;
import com.kartik.Ecommerce.exception.UserException;
import com.kartik.Ecommerce.model.Cart;
import com.kartik.Ecommerce.model.CartItem;
import com.kartik.Ecommerce.model.Product;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.repositories.CartItemRepository;
import com.kartik.Ecommerce.repositories.CartRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemServiceImplementation implements CartItemService{


    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;
    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice()* cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()* cartItem.getQuantity());

        CartItem createdCartItem = cartItemRepository.save(cartItem);
        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(id);
        User user = userService.findUserById(item.getUserId());

        if(user.getId()==userId){
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity()*item.getProduct().getPrice());
            item.setDiscountedPrice(item.getQuantity()*item.getProduct().getDiscountedPrice());
        }
        else {
            throw new UserException("Cart does not belog to user - "+user);
        }
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId) {
        CartItem cartItem = cartItemRepository.isCartItemExists(cart,product,size,userId);
        return cartItem;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem = findCartItemById(cartItemId);
        User user = userService.findUserById(cartItem.getUserId());
        User reqUser = userService.findUserById(userId);
        if(user.getId()== reqUser.getId()){
            cartItemRepository.deleteById(cartItemId);
        }
        throw new CartItemException("You can't delete this item from cart");

    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepository.findById(cartItemId);

        if(opt.isPresent()){
            return opt.get();
        }
        throw new CartItemException("cart Item not found with this id:"+cartItemId);
    }
}
