package com.test.api.service;

import com.test.api.user.Gender;

public interface GenderService {

    void addGender(Gender gender);

    Gender updateGender(Integer id, Gender gender);

    Integer deleteGenderById(Integer id);

    void checkGenderTable();
}
