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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GenderServiceImpl implements GenderService{

    @Autowired
    private GenderRepository genderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Gender addGender(Gender gender) throws ConstraintViolationException {

        try {
            genderRepository.save(gender);
            return gender;
        }
        catch (ConstraintViolationException valEx){
            throw new ValidException(valEx);
        }

    }


    @Override
    public Gender updateGender(Integer id, @Valid @NotNull Gender gender) throws HttpClientErrorException, ConstraintViolationException {

        Gender genderDB = genderRepository.findById(id).orElseThrow(
                () -> new IdNotFoundException("Gender not found, id: " + id));

        if(Objects.nonNull(gender.getName()) &&
                !"".equalsIgnoreCase(gender.getName())) {
            genderDB.setName(gender.getName());

            try{
                genderRepository.save(genderDB);
                return gender;
            }
            catch (ConstraintViolationException valEx){
                throw new ValidException(valEx);
            }
            catch (HttpClientErrorException httpClErrEx){
                throw new BadClientRequestException(httpClErrEx.getMessage());
            }
        }
        throw new OurServiceErrorException("Server error while updating gender");

    }

    @Override
    public Gender deleteGenderById(Integer id) throws HttpClientErrorException, IdNotFoundException {


        Gender genderToDelete = genderRepository.findById(id).orElseThrow(
                () -> new IdNotFoundException("Gender not found, id: " + id));

        List<User> userListWithDeletedGender = userRepository.findByGender(genderToDelete);

        if(!userListWithDeletedGender.isEmpty()){

            for(User user: userListWithDeletedGender){

                user.setGender(null);
                userRepository.save(user);
            }
        }

        genderRepository.deleteById(id);
        if(!genderRepository.existsById(id)){
            return genderToDelete;
        }
        throw new OurServiceErrorException("Server error while deleting gender by id");
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
        else throw new ServerDBException("Error with gender table content");

    }
}
