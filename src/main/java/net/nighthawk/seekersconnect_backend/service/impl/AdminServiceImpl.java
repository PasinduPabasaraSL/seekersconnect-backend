package net.nighthawk.seekersconnect_backend.service.impl;

import net.nighthawk.seekersconnect_backend.dto.UserDto;
import net.nighthawk.seekersconnect_backend.repo.AdminRepo;
import net.nighthawk.seekersconnect_backend.repo.UserRepo;
import net.nighthawk.seekersconnect_backend.service.AdminService;
import net.nighthawk.seekersconnect_backend.service.auth.JWTService;
import net.nighthawk.seekersconnect_backend.utils.Converter;
import net.nighthawk.seekersconnect_backend.utils.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepo;
    private final Converter converter;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    @Autowired
    public AdminServiceImpl(AdminRepo adminRepo, Converter converter, AuthenticationManager authManager, JWTService jwtService) {
        this.adminRepo = adminRepo;
        this.converter = converter;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }


    public String login(UserDto userDto) {
        Authentication auth =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userDto.getUsername(), userDto.getPassword()
                        )
                );

        if (auth.isAuthenticated()) {
            String token = jwtService.generateToken(userDto.getUsername(), userDto.getRole().toString());

            UserRoles extractedRole = jwtService.extractRole(token);
            System.out.println("Extracted Role from Token: " + extractedRole);

            return token;
        }

        return "Invalid username or password";

    }
}
