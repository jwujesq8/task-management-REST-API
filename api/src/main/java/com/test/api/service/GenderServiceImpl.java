package com.test.api.service;

import com.test.api.exception.BadRequestException;
import com.test.api.exception.ServerException;
import com.test.api.exception.ValidException;
import com.test.api.repository.GenderRepository;
import com.test.api.repository.UserRepository;
import com.test.api.service.interfaces.GenderService;
import com.test.api.user.Gender;
import com.test.api.user.User;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Validated
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;
    private final UserRepository userRepository;

    @Override
    public void addGender(@Valid Gender gender) {

        try {
            genderRepository.save(gender);
            if(!genderRepository.existsById(gender.getId())){
                throw new ServerException("Server error while saving new gender");
            }
        }
        catch (ConstraintViolationException valEx){
            throw new ValidException(valEx);
        }

    }


    @Override
    public void updateGender(@Valid Gender gender) {

        Gender genderDB = genderRepository.findById(gender.getId()).orElseThrow(
                () -> new BadRequestException("Gender not found, id: " + gender.getId()));

            try{
                genderRepository.save(genderDB);
                if(!Objects.equals(genderRepository.findByName(gender.getName()).getId(), gender.getId())){
                    throw new ServerException("Server error while updating gender info");
                }
            }
            catch (ConstraintViolationException valEx){
                throw new ValidException(valEx);
            }
            catch (HttpClientErrorException httpClErrEx){
                throw new BadRequestException(httpClErrEx.getMessage());
            }
    }


    @Override
    public void deleteGenderById(Integer id){

        Gender genderToDelete = genderRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Gender not found, id: " + id));

        List<User> userListWithDeletedGender = userRepository.findByGender(genderToDelete);

        if(!userListWithDeletedGender.isEmpty()){

            for(User user: userListWithDeletedGender){

                user.setGender(null);
                userRepository.save(user);
            }
        }

        genderRepository.deleteById(id);

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
            throw new ServerException("Error with gender table content");
        }

    }
}
