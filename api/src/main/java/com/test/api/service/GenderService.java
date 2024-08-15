package com.test.api.service;

import com.test.api.user.Gender;

public interface GenderService {

    Gender addGender(Gender gender);

    Gender updateGender(Integer id, Gender gender);

    Gender deleteGenderById(Integer id);

    void checkGenderTable();
}
