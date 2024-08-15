package com.test.api.modelMapper;

import com.test.api.dto.request.UserRequestDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class User_UserResponseDTO_UserRequestDTO_Mapper {

    private final ModelMapper userRequestDtoToUserMapper = new ModelMapper();
    private final ModelMapper userToUserResponseDtoMapper = new ModelMapper();
    private final ModelMapper userToUserRequestDtoMapper = new ModelMapper();

    public void setUserRequestDtoToUserMapper() {
        userRequestDtoToUserMapper.getConfiguration().setMethodAccessLevel(Configuration.AccessLevel.PRIVATE);
        userRequestDtoToUserMapper.getConfiguration().setSkipNullEnabled(true);

    }

    public void setUserToUserResponseDtoMapper(){
        this.userToUserResponseDtoMapper.getConfiguration().setSkipNullEnabled(true);
        this.userToUserResponseDtoMapper.getConfiguration().setMethodAccessLevel(Configuration.AccessLevel.PRIVATE);
        TypeMap<User, UserResponseDto> propertyMapper = this.userToUserResponseDtoMapper
                .createTypeMap(User.class, UserResponseDto.class);
        propertyMapper.addMappings(mapper -> mapper.map(user -> user.getGender().getName(), UserResponseDto::setGender));
    }

    public void setUserToUserRequestDtoMapper(){
        userRequestDtoToUserMapper.getConfiguration().setMethodAccessLevel(Configuration.AccessLevel.PRIVATE);
        userRequestDtoToUserMapper.getConfiguration().setSkipNullEnabled(true);
    }

}