package com.lothuialon.userservice.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";


}
