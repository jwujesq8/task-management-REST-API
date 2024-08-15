package com.test.api.modelMapper;

import com.test.api.dto.request.UserRequestDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.user.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;

public class UserToUserRequestDTOMapper {

    public UserToUserRequestDTOMapper(ModelMapper userRequestDtoToUserMapper) {

//        TypeMap<User, UserRequestDto> propertyMapper = this.userToUserRequestDTOMapper
//                .createTypeMap(User.class, UserRequestDto.class);
//        propertyMapper.addMappings(mapper -> mapper.map(user -> user.getGender().getName(), UserRequestDto::setGender));
    }
}
