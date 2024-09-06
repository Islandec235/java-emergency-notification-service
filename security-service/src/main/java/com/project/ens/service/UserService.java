package com.project.ens.service;

import com.project.ens.dto.NewUserDto;
import com.project.ens.dto.UserDto;

public interface UserService {
    UserDto create(NewUserDto newUser);

    UserDto getByEmail(String email);
}
