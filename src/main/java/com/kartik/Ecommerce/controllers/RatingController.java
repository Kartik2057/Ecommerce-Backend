package com.kartik.Ecommerce.controllers;


import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.exception.UserException;
import com.kartik.Ecommerce.model.Rating;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.requests.RatingRequest;
import com.kartik.Ecommerce.services.RatingService;
import com.kartik.Ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(
            @RequestBody RatingRequest req,
            @RequestHeader("Authorizartion") String jwt)throws UserException, ProductException {

        User user = userService.findUserProfileByJwt(jwt);
        Rating rating =ratingService.createRating(req,user);
        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductsRating(
            @PathVariable Long productId,
            @RequestHeader("Authorizartion") String jwt
    )throws UserException,ProductException{
        User user = userService.findUserProfileByJwt(jwt);
        List<Rating> ratings = ratingService.getProductsRating(productId);
        return new ResponseEntity<>(ratings,HttpStatus.OK);
    }
}
