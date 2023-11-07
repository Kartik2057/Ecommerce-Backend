package com.kartik.Ecommerce.services;


import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.model.Rating;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.requests.RatingRequest;

import java.util.List;

public interface RatingService {
    public Rating createRating(RatingRequest req, User user)throws ProductException;
    public List<Rating> getProductsRating(Long productId);
}
