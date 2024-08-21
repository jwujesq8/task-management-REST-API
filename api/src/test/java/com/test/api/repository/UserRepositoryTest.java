package com.test.api.repository;

import com.test.api.user.Gender;
import com.test.api.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenderRepository genderRepository;

    @BeforeEach
    void setUp() {

        if(genderRepository.count() == 0){

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

         if(userRepository.count() == 0){

             User user1 = User.builder()
                     .login("euhfo@gmail.com")
                     .password("QKNE740_1k")
                     .fullName("Uioe Wbie")
                     .gender(genderRepository.findByName("male"))
                     .build();
             User user2 = User.builder()
                     .login("or@gmail.com")
                     .password("uido@9403h")
                     .fullName("Ornge Ddsor")
                     .gender(genderRepository.findByName("male"))
                     .build();
             User user3 = User.builder()
                     .login("fpwfnIW@gmail.com")
                     .password("OIhf62_23")
                     .fullName("Sadk Dpasw")
                     .gender(genderRepository.findByName("male"))
                     .build();
             User user4 = User.builder()
                     .login("fnw48@gmail.com")
                     .password("OPoenf!3045")
                     .fullName("Laoda Spays")
                     .gender(genderRepository.findByName("female"))
                     .build();
             User user5 = User.builder()
                     .login("opkw6305@gmail.com")
                     .password("Hkpf444%2")
                     .fullName("Opena Jopena")
                     .gender(genderRepository.findByName("female"))
                     .build();
             User user6 = User.builder()
                     .login("none@gmail.com")
                     .password("NONE9999-")
                     .fullName("None None")
                     .gender(genderRepository.findByName("none"))
                     .build();

             userRepository.save(user1);
             userRepository.save(user2);
             userRepository.save(user3);
             userRepository.save(user4);
             userRepository.save(user5);
             userRepository.save(user6);

         }

    }

    @Test
    void getUserGender(){

        for(User user: userRepository.findAll()){
            System.out.println("fullName: " + user.getFullName() + ", gender: " + user.getGender().getName());
        }

    }

    @Test
    void removeUser_genderIsNotRemoved(){

        long usersCountBeforeRemoving = userRepository.count();
        long idUserToRemove = userRepository.count()-1; // remove the prev last one (female)
        Gender genderOfTheRemovedUser = userRepository.findById(idUserToRemove).get().getGender();

        userRepository.deleteById(idUserToRemove);

        assertEquals(usersCountBeforeRemoving, userRepository.count()+1);
        assertTrue(genderRepository.existsById(genderOfTheRemovedUser.getId()));
    }

    @Test
    void findByGender(){

        Gender gender_male = genderRepository.findByName("male");
        assert gender_male != null;
        List<User> maleList = userRepository.findByGender(gender_male);

        if(!maleList.isEmpty()){

            for(User user: maleList){

                assertEquals(gender_male.getId(), user.getGender().getId());
                System.out.println("user id: " + user.getId() + ", gender: " + user.getGender().getName());
            }
        }

    }

    @Test
    void existsByLoginAndPasswordIgnoreCase(){

        assertFalse(userRepository.existsByLoginAndPasswordIgnoreCase("",""));

        assertTrue(userRepository.existsByLoginAndPasswordIgnoreCase("euhfo@gmail.com","QKNE740_1k"));

        assertTrue(userRepository.existsByLoginAndPasswordIgnoreCase("euhfo@gmail.com","qkne740_1k"));

    }



}