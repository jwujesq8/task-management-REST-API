package com.test.api.service.interfaces;

import com.test.api.user.Gender;
import jakarta.validation.Valid;

public interface GenderService {

    void addGender(@Valid Gender gender);

    void updateGender(@Valid Gender gender);

    void deleteGenderById(Integer id);

    void checkGenderTable();
}
