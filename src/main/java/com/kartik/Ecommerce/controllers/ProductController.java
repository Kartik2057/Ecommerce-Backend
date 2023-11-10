package com.kartik.Ecommerce.controllers;


import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.model.Product;
import com.kartik.Ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<Page<Product>> findProductByCategory(       @RequestParam String category,
                                                                      @RequestParam List<String> color,
                                                                      @RequestParam List<String> size,
                                                                      @RequestParam Integer minPrice,
                                                                      @RequestParam Integer maxPrice,
                                                                      @RequestParam Integer minDiscount,
                                                                      @RequestParam String sort,
                                                                      @RequestParam String stock,
                                                                      @RequestParam Integer pageNumber,
                                                                      @RequestParam Integer pageSize)throws ProductException{
        Page<Product> res = productService.filterProducts(category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
//        System.out.println("complete products");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/id/{productId}")
    public ResponseEntity<Product> findProductById(@PathVariable Long productId)throws ProductException{
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product,HttpStatus.ACCEPTED);
    }
}
