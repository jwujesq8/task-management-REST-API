package com.test.api.modelMapper;


import com.test.api.dto.request.POSTUserRequestDto;
import com.test.api.dto.request.PUTUserRequestDto;
import com.test.api.dto.request.UserRequestDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.exception.ServerException;
import com.test.api.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserModelMapper {

    protected final ModelMapper mapper;

    public UserModelMapper() {
        this.mapper = new ModelMapper();
    }

    public User convert_UserRequestDto_to_User(UserRequestDto userRequestDto){
        try{
            return mapper.map(userRequestDto, User.class);
        }
        catch (IllegalArgumentException e) {
            throw new ServerException.ModelMappingException("Invalid argument provided for mapping: " + e.getMessage());
        }
        catch (org.modelmapper.MappingException e) {
            throw new ServerException.ModelMappingException("Mapping failed: " + e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Unexpected error during mapping: " + e.getMessage());
        }
    }

    public User convert_POSTUserRequestDto_to_User(POSTUserRequestDto postUserRequestDto){
        try{
            return mapper.map(postUserRequestDto, User.class);
        }
        catch (IllegalArgumentException e) {
            throw new ServerException.ModelMappingException("Invalid argument provided for mapping: " + e.getMessage());
        }
        catch (org.modelmapper.MappingException e) {
            throw new ServerException.ModelMappingException("Mapping failed: " + e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Unexpected error during mapping: " + e.getMessage());
        }
    }

    public User convert_PUTUserRequestDto_to_User(PUTUserRequestDto putUserRequestDto){
        try{
            return mapper.map(putUserRequestDto, User.class);
        }
        catch (IllegalArgumentException e) {
            throw new ServerException.ModelMappingException("Invalid argument provided for mapping: " + e.getMessage());
        }
        catch (org.modelmapper.MappingException e) {
            throw new ServerException.ModelMappingException("Mapping failed: " + e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Unexpected error during mapping: " + e.getMessage());
        }
    }





    public UserResponseDto convert_User_to_UserResponseDto(User user){
        try{
            return mapper.map(user, UserResponseDto.class);
        }
        catch (IllegalArgumentException e) {
            throw new ServerException.ModelMappingException("Invalid argument provided for mapping: " + e.getMessage());
        }
        catch (org.modelmapper.MappingException e) {
            throw new ServerException.ModelMappingException("Mapping failed: " + e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Unexpected error during mapping: " + e.getMessage());
        }
    }

    public UserRequestDto convert_User_to_UserRequestDto(User user){
        try{
            return mapper.map(user, UserRequestDto.class);
        }
        catch (IllegalArgumentException e) {
            throw new ServerException.ModelMappingException("Invalid argument provided for mapping: " + e.getMessage());
        }
        catch (org.modelmapper.MappingException e) {
            throw new ServerException.ModelMappingException("Mapping failed: " + e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Unexpected error during mapping: " + e.getMessage());
        }
    }
}
