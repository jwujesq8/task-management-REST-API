package com.test.api.dto.response;

import com.test.api.dto.ViolationDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponseDto {
    private final List<ViolationDto> violations;
}


