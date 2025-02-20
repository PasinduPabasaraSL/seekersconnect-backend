package net.nighthawk.seekersconnect_backend.service;
import net.nighthawk.seekersconnect_backend.dto.UserDto;
import java.util.List;

public interface UserService {
    void registerUser(UserDto userDto);
    String login(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto deleteUser(String username);
}