package com.music.ms_user.mapper;

import org.springframework.stereotype.Component;

import com.music.ms_user.domain.dto.res.UserDtoRes;
import com.music.ms_user.domain.entity.User;

@Component
public class UserMapper {
    public UserDtoRes toUserDto(User user) {
        UserDtoRes userDto = new UserDtoRes();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setProfileImage(user.getProfileImage());
        return userDto;
    }
}
