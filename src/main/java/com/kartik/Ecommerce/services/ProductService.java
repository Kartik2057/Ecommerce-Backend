package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.model.Product;
import com.kartik.Ecommerce.requests.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest request);
    public String deleteProduct(Long productId)throws ProductException;
    public Product updateProduct(Long productId, Product newProduct)throws ProductException;
    public Product findProductById(Long id)throws ProductException;
    public List<Product> findProductByCategory(String category);
    public Page<Product> filterProducts(String category,
                                        List<String> colors,
                                        List<String> sizes,
                                        Integer minPrice,
                                        Integer maxPrice,
                                        Integer minDiscount,
                                        String sort,
                                        String stock,
                                        Integer pageNo,
                                        Integer pageSize);

    List<Product> findAllProducts();
}
