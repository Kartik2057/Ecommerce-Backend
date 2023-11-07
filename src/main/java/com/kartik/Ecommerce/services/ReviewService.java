package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.model.Review;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.requests.ReviewRequest;

import java.util.List;

public interface ReviewService {
    public Review createReview(ReviewRequest req, User user)throws ProductException;
    public List<Review> getAllReviews(Long productId);
}
