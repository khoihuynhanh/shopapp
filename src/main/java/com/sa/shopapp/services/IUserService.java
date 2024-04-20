package com.sa.shopapp.services;

import com.sa.shopapp.dtos.UserDTO;
import com.sa.shopapp.exceptions.DataNotFoundException;
import com.sa.shopapp.models.User;
import org.springframework.stereotype.Service;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password) throws Exception;
}
