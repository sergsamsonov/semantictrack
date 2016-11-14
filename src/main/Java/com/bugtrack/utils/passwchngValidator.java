package com.bugtrack.utils;

import com.bugtrack.entity.passwordChange;
import org.apache.spark.ml.feature.Word2VecModel;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The passwchngValidator provides methods
 * to validate passwordChange objects
 * @version 0.9.9 30 July 2016
 * @author  Sergey Samsonov
 */
@Component
public class passwchngValidator implements Validator {
    @Override
    public boolean supports(Class<?> type) {
        return passwordChange.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        passwordChange passwordChange = (passwordChange) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldPassword", "oldPassword.required",
                "Old password must not be empty");
        if((passwordChange.getPassword() == null)||passwordChange.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.required", "Password must not be empty");
        } else if ((passwordChange.getOldPassword() != null)&&!passwordChange.getOldPassword().isEmpty()) {
            if ((passwordChange.getConfirmPassword() == null) || passwordChange.getConfirmPassword().isEmpty()) {
                errors.rejectValue("confirmPassword", "confirmPassword.required",
                        "Please repeat new password in the field 'Confirm password'");
            } else if (!passwordChange.getPassword().trim().equals(passwordChange.getConfirmPassword().trim())) {
                errors.rejectValue("confirmPassword", "confirmPassword.required", "New passwords don't match");
            } else if (passwordChange.getPassword().trim().length() < 8) {
                errors.rejectValue("password", "password.required", "Password must be at least 8 characters");
            } else {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                if (!passwordEncoder.matches(passwordChange.getOldPassword().trim(),
                        passwordChange.getRightPassword().trim())) {
                    errors.rejectValue("oldPassword", "oldPassword.required", "Passwords don't match");
                }
            }
        }
    }
}
