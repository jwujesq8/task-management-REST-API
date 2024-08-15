package com.test.api.service;

import com.test.api.entity.ResponseMessage;
import com.test.api.repository.GenderRepository;
import com.test.api.repository.UserRepository;
import com.test.api.user.Gender;
import com.test.api.user.User;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;

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
    public Gender addGender(Gender gender) throws ValidationException {
        genderRepository.save(gender);
        if(genderRepository.findByName(gender.getName()) != null){

            return gender;
        }
        throw new ValidationException("""
                Gender requirements:
                \tid|name
                \t1: male
                \t2: female
                \t3: none""");

    }


    @Override
    public Gender updateGender(Integer id, Gender gender) throws HttpClientErrorException {

        Gender genderDB = genderRepository.findById(id).get();

        if(Objects.nonNull(gender.getName()) &&
                !"".equalsIgnoreCase(gender.getName())) {
            genderDB.setName(gender.getName());

            try{
                genderRepository.save(genderDB);
                return gender;
            }
            catch (ValidationException valEx){
                throw new ValidationException("Non valid gender name, name: " + gender.getName());
            }
        }
        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Gender not found, id: " + id);

    }

    @Override
    public Gender deleteGenderById(Integer id) throws HttpClientErrorException {

        Gender genderToDelete = genderRepository.findById(id).get();
        List<User> userListWithDeletedGender = userRepository.findByGender(Optional.of(genderToDelete));

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
        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Gender not found, id: " + id);
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

    }
}
