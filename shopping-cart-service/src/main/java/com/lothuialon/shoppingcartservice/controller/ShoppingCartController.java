package com.lothuialon.shoppingcartservice.controller;

import java.util.List;
import java.util.Map;

import com.lothuialon.shoppingcartservice.dto.CartDTO;
import com.lothuialon.shoppingcartservice.dto.ProductDTO;
import com.lothuialon.shoppingcartservice.entity.Cart;
import com.lothuialon.shoppingcartservice.entity.Product;
import com.lothuialon.shoppingcartservice.service.ShoppingCartService;
import com.lothuialon.shoppingcartservice.utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;


    @PostMapping("/create-cart")
    public ResponseEntity<CartDTO> sepetiOlustur(@RequestParam("name")String name, HttpServletRequest request){
        
        return shoppingCartService.createCart(name, request);
    }

    @PostMapping("")
    public ResponseEntity<CartDTO> sepeteUrunleriEkle(@RequestBody List<ProductDTO> products,
                                                   HttpServletRequest request){

        return shoppingCartService.addProducts(products, request);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<CartDTO> urunleriSepettenCikar(@RequestBody List<Long> productIds,
                                                      HttpServletRequest request){

    return shoppingCartService.removeProducts(productIds, request);

    }

    @DeleteMapping("")
    public ResponseEntity<CartDTO> tumUrunleriSepettenCikar(HttpServletRequest request){

    return shoppingCartService.removeProducts(request);

    }


    @GetMapping("/total-price")
    public ResponseEntity<Map<String,String>> toplamFiyat(HttpServletRequest request) {

        return shoppingCartService.getShoppingCartPrice(request);
    }

    @GetMapping("")
    public ResponseEntity<CartDTO> sepet(HttpServletRequest request) {

        return ResponseEntity.ok().body(shoppingCartService.getProductsInCart(request));
    }

    @GetMapping("/cart-items")
    public ResponseEntity<List<ProductDTO>> tumUrunler(HttpServletRequest request) {

        return ResponseEntity.ok().body(shoppingCartService.getCart(request));
    }

}