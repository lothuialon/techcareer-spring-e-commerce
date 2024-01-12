package com.lothuialon.userservice.service.Impl;

import com.lothuialon.userservice.DTO.loginDTO;
import com.lothuialon.userservice.DTO.registerDTO;
import com.lothuialon.userservice.entity.Role;
import com.lothuialon.userservice.entity.User;
import com.lothuialon.userservice.repository.RoleRepository;
import com.lothuialon.userservice.repository.UserRepository;
import com.lothuialon.userservice.security.JwtTokenProvider;
import com.lothuialon.userservice.service.authService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Service
public class authServiceImpl implements authService {


    private AuthenticationManager authenticationManager;
    private UserRepository theUserRepository;
    private RoleRepository theRoleRepository;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder thePasswordEncoder;

    @Autowired
    public authServiceImpl(AuthenticationManager authenticationManager, UserRepository theUserRepository, RoleRepository roleRepository, PasswordEncoder thePasswordEncoder, JwtTokenProvider theJwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.theUserRepository = theUserRepository;
        this.theRoleRepository = roleRepository;
        this.thePasswordEncoder = thePasswordEncoder;
        this.jwtTokenProvider = theJwtTokenProvider;
    }



    @Override
    public String login(loginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsernameOrEmail(),
                loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        int userId = theUserRepository.findByUsernameOrEmail(loginDTO.getUsernameOrEmail(),
                loginDTO.getUsernameOrEmail()).get().getId();

        return jwtTokenProvider.generateToken(authentication, userId);
    }

    @Override
    public String register(registerDTO registerDTO) {

        if(theUserRepository.existsByUsername(registerDTO.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        if(theUserRepository.existsByEmail(registerDTO.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(registerDTO.getUsername());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPassword(thePasswordEncoder.encode(registerDTO.getPassword()));

        List<Role> roles = new ArrayList<>();

        Role theRole = theRoleRepository.findByName("ROLE_USER").get();
        roles.add(theRole);
        newUser.setRoles(roles);
        theUserRepository.save(newUser);
        return "User is registered";

    }

}
