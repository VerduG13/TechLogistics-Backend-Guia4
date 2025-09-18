package org.ean.techlogistics.service;

import lombok.RequiredArgsConstructor;
import org.ean.techlogistics.entity.Product;
import org.ean.techlogistics.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        if(productRepository.existsByCode(product.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product code already exists");
        }
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getByCode(String code) {
        return productRepository.findByCode(code);
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void adjustStock(Long productId, int delta) {
        productRepository.adjustStock(productId, delta);
    }
}
