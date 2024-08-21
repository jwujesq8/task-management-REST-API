package com.test.api.annotation.validateStartEndId;
import com.test.api.dto.DeleteUsersListByIdDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidStartEndIdValidator implements ConstraintValidator<ValidStartEndId, DeleteUsersListByIdDto>{


    @Override
    public boolean isValid(DeleteUsersListByIdDto dto, ConstraintValidatorContext context) {
        if (dto.getStartId() == null || dto.getEndId() == null) {
            return true;
        }
        return dto.getEndId()>=dto.getStartId();

    }


}


