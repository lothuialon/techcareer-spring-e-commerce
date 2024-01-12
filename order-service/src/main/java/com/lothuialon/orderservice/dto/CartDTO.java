package com.lothuialon.orderservice.dto;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartDTO {
    
    private Long id;
    private Long userId;
    private String CartName;
    
    Set<ProductDTO> products;

}

