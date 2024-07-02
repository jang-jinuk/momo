/*package com.momo.momopjt.user;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class MBTIValidatorImpl implements ConstraintValidator<ValidMBTI, String> {

    private final List<String> validMBTITypes = Arrays.asList(
            "INTJ", "INTP", "ENTJ", "ENTP",
            "INFJ", "INFP", "ENFJ", "ENFP",
            "ISTJ", "ISFJ", "ESTJ", "ESFJ",
            "ISTP", "ISFP", "ESTP", "ESFP"
    );

    @Override
    public void initialize(ValidMBTI constraint) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validMBTITypes.contains(value);
    }
}

 */