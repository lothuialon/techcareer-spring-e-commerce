package com.lothuialon.productservice.controller;

import com.lothuialon.productservice.dto.ProductDTO;
import com.lothuialon.productservice.entity.Product;
import com.lothuialon.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return productService.getAll();
    }

    @PostMapping()
    public ResponseEntity<ProductDTO> saveProduct(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }
}
