package com.test.api.modelMapper;

import com.test.api.dto.response.UserResponseDto;
import com.test.api.user.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class ModelMapperConfig {

    @Bean
    public UserModelMapper userModelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setMethodAccessLevel(Configuration.AccessLevel.PRIVATE);

        TypeMap<User, UserResponseDto> propertyMapper = modelMapper
                .createTypeMap(User.class, UserResponseDto.class);
        propertyMapper.addMappings(mapper -> mapper.map(user -> user.getGender().getName(), UserResponseDto::setGenderName));

        return new UserModelMapper(modelMapper);
    }
}
