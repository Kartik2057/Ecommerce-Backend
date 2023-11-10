package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.ProductException;
import com.kartik.Ecommerce.model.Category;
import com.kartik.Ecommerce.model.Product;
import com.kartik.Ecommerce.repositories.CategoryRepository;
import com.kartik.Ecommerce.repositories.ProductRepository;
import com.kartik.Ecommerce.requests.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest request) {

        Category firstLevel = categoryRepository.findByName(request.getFirstLevelCategory());
        if (firstLevel == null) {
            Category firstLevelCategory = new Category();
            firstLevelCategory.setName(request.getFirstLevelCategory());
            firstLevelCategory.setLevel(1);
            firstLevel = categoryRepository.save(firstLevelCategory);
        }
        Category secondLevel = categoryRepository.findByNameAndParent(request.getSecondLevelCategory(), firstLevel.getName());
        if (secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(request.getSecondLevelCategory());
            secondLevelCategory.setLevel(2);
            secondLevelCategory.setParentCategory(firstLevel);
            secondLevel = categoryRepository.save(secondLevelCategory);
        }
        Category thirdLevel = categoryRepository.findByNameAndParent(request.getThirdLevelCategory(), secondLevel.getName());
        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(request.getThirdLevelCategory());
            thirdLevelCategory.setLevel(3);
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();

        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setDiscountPercent(request.getDiscountPercent());
        product.setImageUrl(request.getImageUrl());
        product.setPrice(request.getPrice());
        product.setSizes(request.getSizes());
        product.setQuantity(request.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        System.out.println("products - "+product);
        return savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product=findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product deleted Successfully";
    }

    @Override
    public Product updateProduct(Long productId, Product newProduct) throws ProductException {
        Product product=findProductById(productId);
        if(newProduct.getQuantity()!=product.getQuantity())
            product.setQuantity(newProduct.getQuantity());
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt = productRepository.findById(id);
        
        if(opt.isPresent())
            return opt.get();
        throw new ProductException("Product not found with id - " + id);
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return null;
    }

    @Override
    public Page<Product> filterProducts(
            String category,
            List<String> colors,
            List<String> sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNo,
            Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);

        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount,sort);
//        System.out.println(products);
        if (!colors.isEmpty()){
            products=products
                    .stream()
                    .filter(p->colors
                            .stream()
                            .anyMatch(
                                    c->c.equalsIgnoreCase(p.getColor()
                            )))
                    .collect(Collectors.toList());
        }

        if(stock.equals("in_stock")){
                products = products
                        .stream()
                        .filter(
                                p->p.getQuantity()>0
                        ).collect(Collectors.toList());
        } else if (stock.equals("out_of_stock")) {
                products = products
                        .stream()
                        .filter(
                                p->p.getQuantity()<1
                         ).collect(Collectors.toList());
        }
        if(sort.equals("price_low"))
            Collections.sort(products, Comparator.comparingInt(Product::getDiscountedPrice));
        else
            Collections.sort(products, Comparator.comparingInt(Product::getDiscountedPrice).reversed());

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex+pageable.getPageSize(),products.size());

        List<Product> pageContent = products.subList(startIndex,endIndex);

        Page<Product> filteredProducts = new PageImpl<>(pageContent,pageable, products.size());
        return filteredProducts;
    }

    @Override
    public List<Product> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }
}
