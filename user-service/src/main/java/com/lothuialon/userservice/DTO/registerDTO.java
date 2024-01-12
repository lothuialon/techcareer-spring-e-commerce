package com.lothuialon.userservice.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class registerDTO {

    private String username;
    private String email;
    private String password;

}
