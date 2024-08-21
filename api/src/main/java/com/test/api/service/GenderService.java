package com.test.api.service;

import com.test.api.user.Gender;
import jakarta.validation.Valid;

public interface GenderService {

    void addGender(@Valid Gender gender);

    void updateGender(@Valid Gender gender);

    void deleteGenderById(Gender gender);

    void checkGenderTable();
}
