package com.test.api.modelMapper;


import com.test.api.dto.request.POSTUserRequestDto;
import com.test.api.dto.request.PUTUserRequestDto;
import com.test.api.dto.request.UserRequestDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.repository.GenderRepository;
import com.test.api.user.Gender;
import com.test.api.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class UserModelMapper {

    private final ModelMapper mapper;

    public User map_POSTUserRequestDto_to_User(POSTUserRequestDto postUserRequestDto) {
        return mapper.map(postUserRequestDto, User.class);
    }

    public User map_PUTUserRequestDto_to_User(PUTUserRequestDto putUserRequestDto) {
        return mapper.map(putUserRequestDto, User.class);
    }

    public UserResponseDto map_User_to_UserResponseDto(User user) {
        return mapper.map(user, UserResponseDto.class);
    }

}
