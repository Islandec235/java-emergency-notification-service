package com.project.ens.mapper;

import com.project.ens.dto.NewUserDto;
import com.project.ens.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUser(NewUserDto dto);
}
