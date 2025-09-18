package org.ean.techlogistics.controller;

import lombok.RequiredArgsConstructor;
import org.ean.techlogistics.dto.ProductDTO;
import org.ean.techlogistics.entity.Product;
import org.ean.techlogistics.mapper.EntityMapper;
import org.ean.techlogistics.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO dto) {
        Product product = EntityMapper.toProductEntity(dto);
        Product saved = productService.createProduct(product);
        return ResponseEntity.ok(EntityMapper.toProductDTO(saved));
    }

    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAllProducts().stream().map(EntityMapper::toProductDTO).toList();
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductDTO> getByCode(@PathVariable String code) {
        Optional<Product> product = productService.getByCode(code);
        return product.map(value -> ResponseEntity.ok(EntityMapper.toProductDTO(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDTO> adjustStock(
            @PathVariable Long id,
            @RequestParam int delta
    ) {
        Optional<Product> product = productService.getById(id);
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productService.adjustStock(id, delta);
        return ResponseEntity.noContent().build();
    }
}
