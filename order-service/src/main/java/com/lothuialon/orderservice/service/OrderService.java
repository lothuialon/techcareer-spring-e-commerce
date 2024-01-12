package com.lothuialon.orderservice.service;



import jakarta.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lothuialon.orderservice.dto.CartDTO;
import com.lothuialon.orderservice.entity.Order;
import com.lothuialon.orderservice.entity.Product;
import com.lothuialon.orderservice.repository.OrderRepository;
import com.lothuialon.orderservice.utility.JwtUtil;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderService shoppingCartService;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderService shoppingCartService, RestTemplate restTemplate, JwtUtil jwtUtil) {
        this.orderRepository = orderRepository;
        this.shoppingCartService = shoppingCartService;
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
    }



    public Order createOrder(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtUtil.getToken(request));
        HttpEntity<String> entity = new HttpEntity<>("null", headers);

        ResponseEntity<CartDTO> response = restTemplate.exchange(
            "http://SHOPPING-CART-SERVICE/api/cart", HttpMethod.GET, entity, CartDTO.class);

        //every user has a cart, it is created when a user is registered

        Set<Product> products = response.getBody().getProducts().stream()
            .map(productDTO -> Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .build())
            .collect(Collectors.toSet());

        Order order = Order.builder()
            .userId(userId)
            .orderDate(new Date())
            .products(products)
            .build();


        return orderRepository.save(order);
    }

    public Order getOrder(Long orderId, HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("No matching result for the given id"));
    }

    public List<Order> getOrders(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));
        return orderRepository.findByUserId(userId);
    }

}

