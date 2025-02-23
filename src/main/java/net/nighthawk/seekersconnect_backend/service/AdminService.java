package net.nighthawk.seekersconnect_backend.service;

import net.nighthawk.seekersconnect_backend.dto.UserDto;
import org.springframework.stereotype.Service;

public interface AdminService {
    String login(UserDto userDto);
}