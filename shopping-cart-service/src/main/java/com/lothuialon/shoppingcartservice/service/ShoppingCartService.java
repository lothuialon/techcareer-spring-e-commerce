package com.lothuialon.shoppingcartservice.service;


import com.lothuialon.shoppingcartservice.dto.CartDTO;
import com.lothuialon.shoppingcartservice.dto.ProductDTO;
import com.lothuialon.shoppingcartservice.entity.Cart;
import com.lothuialon.shoppingcartservice.entity.Product;
import com.lothuialon.shoppingcartservice.repository.ProductRepository;
import com.lothuialon.shoppingcartservice.repository.ShoppingCartRepository;
import com.lothuialon.shoppingcartservice.utility.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository, RestTemplate restTemplate, JwtUtil jwtUtil) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
    }


    
    public ResponseEntity<CartDTO> createCart(String name, HttpServletRequest request)
    {

        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));

        Cart shoppingCart = new Cart();
        shoppingCart.setCartName(name);
        shoppingCart.setUserId(userId);
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        CartDTO cartDTO = CartDTO.builder()
        .CartName(shoppingCart.getCartName())
        .id(shoppingCart.getId())
        .userId(shoppingCart.getUserId())
        .products(shoppingCart.getProducts().stream()
                .map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build())
                .collect(Collectors.toSet()))
        .build();

        return ResponseEntity.ok().body(cartDTO);

    }


    public ResponseEntity<CartDTO> addProducts(List<ProductDTO> productDTOs, HttpServletRequest request)
    {

        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));
        Cart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(() 
        -> new RuntimeException("Verilen id ile eşleşen bir sonuç yok"));;

        List<Product> products = productDTOs.stream()
        .map(productDTO -> Product.builder()
            .id(productDTO.getId())
            .name(productDTO.getName())
            .price(productDTO.getPrice())
            .build())
        .collect(Collectors.toList());
    

        products.forEach(product -> productRepository.saveAndFlush(product));
        Set<Product>  newProducts = new HashSet<>(products);
        shoppingCart.setProducts(newProducts);

        shoppingCart = shoppingCartRepository.save(shoppingCart);

        CartDTO cartDTO = CartDTO.builder()
        .CartName(shoppingCart.getCartName())
        .id(shoppingCart.getId())
        .userId(shoppingCart.getUserId())
        .products(shoppingCart.getProducts().stream()
                .map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build())
                .collect(Collectors.toSet()))
        .build();

        return ResponseEntity.ok().body(cartDTO);


    }

    public ResponseEntity<CartDTO> removeProducts(List<Long> productIds, HttpServletRequest request)
    {

        if(productIds.isEmpty()){
            throw new RuntimeException("Silinecek ürün girdisi yok");
        }

        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));
        Cart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(() 
        -> new RuntimeException("Verilen id ile eşleşen bir sonuç yok"));;
    
        Set<Product> productsToRemove = new HashSet<>(productRepository.findAllById(productIds));
        shoppingCart.getProducts().removeAll(productsToRemove);
        shoppingCart = shoppingCartRepository.save(shoppingCart);


        CartDTO cartDTO = CartDTO.builder()
        .CartName(shoppingCart.getCartName())
        .id(shoppingCart.getId())
        .userId(shoppingCart.getUserId())
        .products(shoppingCart.getProducts().stream()
                .map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build())
                .collect(Collectors.toSet()))
        .build();

        return ResponseEntity.ok().body(cartDTO);
    }

    public ResponseEntity<CartDTO> removeProducts(HttpServletRequest request)
    {


        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));
        Cart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(() 
        -> new RuntimeException("Verilen id ile eşleşen bir sonuç yok"));;
    
        shoppingCart.getProducts().clear();
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        CartDTO cartDTO = CartDTO.builder()
        .CartName(shoppingCart.getCartName())
        .id(shoppingCart.getId())
        .userId(shoppingCart.getUserId())
        .products(shoppingCart.getProducts().stream()
                .map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build())
                .collect(Collectors.toSet()))
        .build();

        return ResponseEntity.ok().body(cartDTO);
    }

    public ResponseEntity<Map<String,String>> getShoppingCartPrice(HttpServletRequest request){

        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));
        Cart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(() 
        -> new RuntimeException("Verilen id ile eşleşen bir sonuç yok"));;

        Map<String,String> response = new HashMap<>();

        int totalPrice = shoppingCart.getProducts().stream().map(product ->
                        restTemplate.getForObject("http://PRODUCT-SERVICE/api/product"+product.getId(),HashMap.class))
                .mapToInt(productResponse -> (int) productResponse.get("price"))
                .sum();
        response.put("Toplam Fiyat", Double.toString(totalPrice));
        return ResponseEntity.ok().body(response);

    }

    public CartDTO getProductsInCart(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));
        Cart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Verilen id ile eşleşen bir sonuç yok"));

        CartDTO cartDTO = CartDTO.builder()
        .CartName(shoppingCart.getCartName())
        .id(shoppingCart.getId())
        .userId(shoppingCart.getUserId())
        .products(shoppingCart.getProducts().stream()
                .map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build())
                .collect(Collectors.toSet()))
        .build();

        return cartDTO;
    }

    public List<ProductDTO> getCart(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(jwtUtil.getToken(request));
        Cart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Verilen id ile eşleşen bir sonuç yok"));

        Set<ProductDTO> productDTOs = shoppingCart.getProducts().stream()
                        .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .build())
                        .collect(Collectors.toSet());


        return new ArrayList<>(productDTOs);
    }
}