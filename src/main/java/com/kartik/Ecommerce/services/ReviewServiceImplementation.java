package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.model.Product;
import com.kartik.Ecommerce.model.Review;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.repositories.ProductRepository;
import com.kartik.Ecommerce.repositories.ReviewRepository;
import com.kartik.Ecommerce.requests.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {
        Product product = productService.findProductById(req.getProductId());
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());
        review.setReview(req.getReview());
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews(Long productId) {
        return reviewRepository.getAllProductReview(productId);
    }
}
