package com.test.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ViolationDto {

    private final String fieldName;
    private final String message;

}
