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

    public final ModelMapper userRequestDtoToUserMapper = new ModelMapper();
    public final ModelMapper userToUserResponseDtoMapper = new ModelMapper();
    public final ModelMapper userToUserRequestDtoMapper = new ModelMapper();

    public User_UserResponseDTO_UserRequestDTO_Mapper(ModelMapper userRequestDtoToUserMapper,
                                                      ModelMapper userToUserResponseDtoMapper,
                                                      ModelMapper userToUserRequestDtoMapper) {

        this.userRequestDtoToUserMapper.getConfiguration().setMethodAccessLevel(Configuration.AccessLevel.PRIVATE);
        this.userRequestDtoToUserMapper.getConfiguration().setSkipNullEnabled(true);

        this.userToUserResponseDtoMapper.getConfiguration().setSkipNullEnabled(true);
        this.userToUserResponseDtoMapper.getConfiguration().setMethodAccessLevel(Configuration.AccessLevel.PRIVATE);
        TypeMap<User, UserResponseDto> propertyMapper = this.userToUserResponseDtoMapper
                .createTypeMap(User.class, UserResponseDto.class);
        propertyMapper.addMappings(mapper -> mapper.map(user -> user.getGender().getName(), UserResponseDto::setGender));

        this.userToUserRequestDtoMapper.getConfiguration().setMethodAccessLevel(Configuration.AccessLevel.PRIVATE);
        this.userToUserRequestDtoMapper.getConfiguration().setSkipNullEnabled(true);

    }


}