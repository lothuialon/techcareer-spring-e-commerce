package com.lothuialon.orderservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lothuialon.orderservice.entity.Product;
import com.lothuialon.orderservice.repository.ProductRepository;



@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<Product> getById(Long id){
        return ResponseEntity.ok(productRepository.findById(id).get());
    }

    public ResponseEntity<List<Product>> getAll(){
        return ResponseEntity.ok().body(productRepository.findAll());
    }

    public ResponseEntity<Product> save(Product product){
        return ResponseEntity.ok().body(productRepository.save(product));
    }
}
