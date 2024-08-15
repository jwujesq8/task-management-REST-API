package com.test.api.modelMapper;

import com.test.api.dto.response.UserResponseDto;
import com.test.api.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;

import java.util.List;

public class UserToUserResponseDTOMapper extends ModelMapper {

    ModelMapper userToUserResponseDTOMapper;

    public UserToUserResponseDTOMapper(ModelMapper userRequestDtoToUserMapper) {

//        propertyMapper.addMappings(mapper -> mapper.when(Conditions.isNull()).skip(User::getGender, UserResponseDto::setGender));
    }


}
