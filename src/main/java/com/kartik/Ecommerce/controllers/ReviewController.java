package com.kartik.Ecommerce.controllers;

import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.exception.UserException;
import com.kartik.Ecommerce.model.Review;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.requests.ReviewRequest;
import com.kartik.Ecommerce.services.ReviewService;
import com.kartik.Ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(
            @RequestBody ReviewRequest req,
            @RequestHeader("Authorization") String jwt
            )throws UserException, ProductException{
        User user = userService.findUserProfileByJwt(jwt);

        Review review = reviewService.createReview(req,user);

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductsReview(
            @RequestParam("productId")Long productId
    )throws UserException,ProductException
    {
        List<Review> reviews = reviewService.getAllReviews(productId);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }
}
