package com.test.api.service;

import com.test.api.exception.*;
import com.test.api.repository.GenderRepository;
import com.test.api.repository.UserRepository;
import com.test.api.user.Gender;
import com.test.api.user.User;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class GenderServiceImpl implements GenderService{

    private final GenderRepository genderRepository;
    private final UserRepository userRepository;

    @Override
    public void addGender(@Valid Gender gender) {

        try {
            genderRepository.save(gender);
            if(!genderRepository.existsById(gender.getId())){
                throw new OurServiceErrorException("Server error while saving new gender");
            }
        }
        catch (ConstraintViolationException valEx){
            throw new ValidException(valEx);
        }

    }


    @Override
    public void updateGender(@Valid Gender gender) {

        Gender genderDB = genderRepository.findById(gender.getId()).orElseThrow(
                () -> new IdNotFoundException("Gender not found, id: " + gender.getId()));

            try{
                genderRepository.save(genderDB);
                if(!Objects.equals(genderRepository.findByName(gender.getName()).getId(), gender.getId())){
                    throw new OurServiceErrorException("Server error while updating gender info");
                }
            }
            catch (ConstraintViolationException valEx){
                throw new ValidException(valEx);
            }
            catch (HttpClientErrorException httpClErrEx){
                throw new BadClientRequestException(httpClErrEx.getMessage());
            }
    }


    @Override
    public void deleteGenderById(Gender gender){

        Gender genderToDelete = genderRepository.findById(gender.getId()).orElseThrow(
                () -> new IdNotFoundException("Gender not found, id: " + gender.getId()));

        List<User> userListWithDeletedGender = userRepository.findByGender(genderToDelete);

        if(!userListWithDeletedGender.isEmpty()){

            for(User user: userListWithDeletedGender){

                user.setGender(null);
                userRepository.save(user);
            }
        }

        genderRepository.deleteById(gender.getId());
        if(genderRepository.existsById(gender.getId())){
            throw new OurServiceErrorException("Server error while deleting gender by id");
        }

    }

    @Override
    public void checkGenderTable(){

        if(genderRepository.count()==0){
            Gender gender_male = Gender.builder()
                    .id(1)
                    .name("male")
                    .build();
            Gender gender_female = Gender.builder()
                    .id(2)
                    .name("female")
                    .build();
            Gender gender_none = Gender.builder()
                    .id(3)
                    .name("none")
                    .build();
            genderRepository.save(gender_male);
            genderRepository.save(gender_female);
            genderRepository.save(gender_none);

        }
        else if(genderRepository.count()!=3){
            throw new ServerDBException("Error with gender table content");
        }

    }
}
