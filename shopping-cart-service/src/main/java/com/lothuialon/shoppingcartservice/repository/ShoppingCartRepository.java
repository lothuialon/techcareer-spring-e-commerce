package com.lothuialon.shoppingcartservice.repository;

import java.util.Optional;

import com.lothuialon.shoppingcartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findById(Long id);
    Optional<Cart> findByUserId(Long UserId);
}