package com.lothuialon.shoppingcartservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lothuialon.shoppingcartservice.dto.ProductDTO;
import com.lothuialon.shoppingcartservice.entity.Product;
import com.lothuialon.shoppingcartservice.repository.ProductRepository;



@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<ProductDTO> getById(Long id){
        
        Product product = productRepository.findById(id).get();
        ProductDTO productDTO = ProductDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .build();

        return ResponseEntity.ok(productDTO);
    }

    public ResponseEntity<List<ProductDTO>> getAll(){

        List<Product> products = productRepository.findAll();

        List<ProductDTO> productDTOs = products.stream()
            .map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build())
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(productDTOs);
    }

    public ResponseEntity<ProductDTO> save(ProductDTO productDTO){

        Product product = Product.builder()
            .id(productDTO.getId())
            .name(productDTO.getName())
            .price(productDTO.getPrice())
            .build();

        Product savedProduct = productRepository.save(product);
        ProductDTO savedProductDTO = ProductDTO.builder()
            .id(savedProduct.getId())
            .name(savedProduct.getName())
            .price(savedProduct.getPrice())
            .build();
        return ResponseEntity.ok().body(savedProductDTO);
    }
}
