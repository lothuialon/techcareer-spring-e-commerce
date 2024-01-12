package com.lothuialon.shoppingcartservice.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class CartDTO {
    
    private Long id;
    private Long userId;
    private String CartName;
    
    Set<ProductDTO> products;

}

