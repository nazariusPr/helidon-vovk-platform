package org.nazarius.mapper;

import org.nazarius.model.User;
import org.nazarius.dto.UserDto;

public class UserMapper {

    // Convert User entity to UserDto
    public static UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    // Convert UserDto to User entity
    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        return new User(
                dto.getId(),
                dto.getUsername(),
                null,
                dto.getRole()
        );
    }
}