package com.lothuialon.shoppingcartservice.repository;

import java.util.Optional;

import com.lothuialon.shoppingcartservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    
    Optional<Product> findById(Long id);

}
