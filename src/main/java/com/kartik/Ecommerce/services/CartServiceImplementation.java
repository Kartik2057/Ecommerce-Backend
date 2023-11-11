package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.CartItemException;
import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.exception.UserException;
import com.kartik.Ecommerce.model.Cart;
import com.kartik.Ecommerce.model.CartItem;
import com.kartik.Ecommerce.model.Product;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.repositories.CartRepository;
import com.kartik.Ecommerce.requests.AddItemRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartServiceImplementation implements CartService{

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ProductService productService;

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public String addCartItem(Long userId, AddItemRequest request) throws ProductException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(request.getProductId());
        CartItem isPresent = cartItemService.isCartItemExists(cart,product,request.getSize(),userId);

        if(isPresent==null){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setUserId(userId);
            cartItem.setQuantity(request.getQuantity());

            int price = request.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(request.getSize());
            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
            cartRepository.save(cart);
            return "Cart Item added successfully";
        }
        return "Cart Item already existed";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        int totalPrice=0;
        int totalDiscountedPrice = 0;
        int totalItem=0;

        System.out.println("cartImple : "+cart.getCartItems());
        for(CartItem cartItem:cart.getCartItems()){
            totalPrice+= cartItem.getPrice();
            totalDiscountedPrice+=cartItem.getDiscountedPrice();
            totalItem+=cartItem.getQuantity();
        }

        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setDiscount(totalPrice-totalDiscountedPrice);

        return cartRepository.save(cart);
    }

//    @Override
//    public String toString() {
//        return "CartItem(id=" + id +
//                ", product=" + product.getTitle() + // Include relevant fields instead of the entire Product
//                ", size=" + size +
//                ", quantity=" + quantity +
//                ", price=" + price +
//                ", discountedPrice=" + discountedPrice +
//                ", userId=" + userId +
//                ")";
//    }
}
