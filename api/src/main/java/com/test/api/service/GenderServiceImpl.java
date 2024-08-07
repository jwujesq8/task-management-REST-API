package com.test.api.service;

import com.test.api.repository.GenderRepository;
import com.test.api.repository.UserRepository;
import com.test.api.user.Gender;
import com.test.api.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void addGender(Gender gender) {
        genderRepository.save(gender);
    }


    @Override
    public Gender updateGender(Integer id, Gender gender) {

        Gender genderDB = genderRepository.findById(id).get();

        if(Objects.nonNull(gender.getName()) &&
                !"".equalsIgnoreCase(gender.getName())) {
            genderDB.setName(gender.getName());

        }

        return genderRepository.save(genderDB);

    }

    @Override
    public Integer deleteGenderById(Integer id) {

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
            return id;
        }
        return null;
    }

    @Override
    public void checkGenderTable() {

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
