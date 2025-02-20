package net.nighthawk.seekersconnect_backend.service.impl;

import net.nighthawk.seekersconnect_backend.dto.LoginDto;
import net.nighthawk.seekersconnect_backend.dto.UserDto;
import net.nighthawk.seekersconnect_backend.entity.User;
import net.nighthawk.seekersconnect_backend.repo.UserRepo;
import net.nighthawk.seekersconnect_backend.service.UserService;
import net.nighthawk.seekersconnect_backend.service.auth.JWTService;
import net.nighthawk.seekersconnect_backend.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final Converter converter;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, Converter converter, AuthenticationManager authManager, JWTService jwtService) {
        this.userRepo = userRepo;
        this.converter = converter;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public void registerUser(UserDto userDto) {
        if (userRepo.existsByUsername(userDto.getUsername()))
            throw new RuntimeException(userDto.getUsername() + " is already exists..!!");
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        userRepo.save(converter.userDtoToEntity(userDto));
    }

    public String login(LoginDto loginDto) {
        Authentication auth =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDto.getUsername(), loginDto.getPassword()
                        )
                );

        if (auth.isAuthenticated())
            return jwtService.generateToken(loginDto.getUsername());

        return "Invalid username or password";

    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepo.findAll();
        return converter.userListToDtoList(users);
    }

    @Override
    public UserDto deleteUser(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null)
            return null;

        userRepo.delete(user);

        return converter.entityToUserDto(user);
    }


}