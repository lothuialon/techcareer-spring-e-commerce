package com.lothuialon.userservice.service;

import com.lothuialon.userservice.DTO.loginDTO;
import com.lothuialon.userservice.DTO.registerDTO;

public interface authService {
    String login(loginDTO loginDTO);
    String register(registerDTO registerDTO);
}