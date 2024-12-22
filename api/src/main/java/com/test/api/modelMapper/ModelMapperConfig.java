package com.test.api.modelMapper;

import com.test.api.dto.request.POSTUserRequestDto;
import com.test.api.dto.request.PUTUserRequestDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.repository.GenderRepository;
import com.test.api.user.Gender;
import com.test.api.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {
    private final GenderRepository genderRepository;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setImplicitMappingEnabled(false);

        Converter<String, Gender> converter_toGenderIdByGenderName = context -> {
            String genderName = context.getSource();
            return genderRepository.findByNameIgnoreCase(genderName).orElse(genderRepository.findByName("none"));
        };

        TypeMap<POSTUserRequestDto, User> postUser_user = mapper.createTypeMap(POSTUserRequestDto.class, User.class);
        postUser_user.addMappings(mapping -> mapping.using(converter_toGenderIdByGenderName)
                .map(POSTUserRequestDto::getGenderName, User::setGender))
                .implicitMappings();


        TypeMap<PUTUserRequestDto, User> putUser_user = mapper.createTypeMap(PUTUserRequestDto.class, User.class);
        putUser_user.addMappings(mapping -> mapping.using(converter_toGenderIdByGenderName)
                .map(PUTUserRequestDto::getGenderName, User::setGender))
                .implicitMappings();


        mapper.typeMap(User.class, UserResponseDto.class)
                .addMappings(mapping ->mapping.map(user -> user.getGender().getName(), UserResponseDto::setGenderName))
                .implicitMappings();


        return mapper;
    }
}