package com.lothuialon.userservice.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class loginDTO {

    private String usernameOrEmail;
    private String password;

}
