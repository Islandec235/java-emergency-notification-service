package com.project.ens.mapper;

import com.project.ens.dto.NewUserDto;
import com.project.ens.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-03T02:35:41+0300",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.jar, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(NewUserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );
        user.setEmail( dto.getEmail() );
        user.setPhone( dto.getPhone() );

        return user;
    }
}
