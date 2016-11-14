package com.bugtrack.utils;

import org.apache.commons.validator.routines.EmailValidator;
import com.bugtrack.entity.user;
import com.bugtrack.service.userService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The userValidator provides methods
 * to validate user's objects
 * @version 0.9.9 31 July 2016
 * @author  Sergey Samsonov
 */
@Component
public class userValidator implements Validator {
    @Override
    public boolean supports(Class<?> type) {
        return user.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        user user = (user) o;
        if(user.getId() == null){
            if((user.getPassword() == null)||user.getPassword().isEmpty()){
                errors.rejectValue("password", "password.required", "Password must not be empty");
            } else {
                if (user.getPassword().length() < 8) {
                    errors.rejectValue("password", "password.required", "Password must be at least 8 characters");
                }
                if((user.getConfirmPassword() == null)||user.getConfirmPassword().isEmpty()){
                    errors.rejectValue("confirmPassword", "confirmPassword.required",
                            "Please repeat new password in the field 'Confirm password'");
                } else if (!user.getPassword().trim().equals(user.getConfirmPassword().trim())) {
                    errors.rejectValue("confirmPassword", "confirmPassword.required", "Passwords don't match");
                }
            }
        }
        if((user.getLogin() == null)||user.getLogin().isEmpty()){
            errors.rejectValue("login", "login.required", "Login must not be empty");
        } else if (user.getLogin().length() > 10) {
            errors.rejectValue("login", "login.required", "Login must be less than 11 characters");
        } else {
            String login = user.getLogin().trim();
            userService userService = new userService();
            user userByLogin = userService.getUserByLogin(login);
            if((userByLogin != null)&&
               ((user.getId() == null)||(user.getId() != userByLogin.getId()))) {
                errors.rejectValue("login", "login.required",
                        "User with login " + login + " already exists. Please enter other login.");
            }
        }
        if((user.getEmail() == null)||user.getEmail().isEmpty()){
            errors.rejectValue("email", "email.required", "Email field must not be empty");
        } else if( !EmailValidator.getInstance().isValid(user.getEmail())){
            errors.rejectValue("email", "email.notValid", "Email address is not valid.");
        }
        if((user.getFirstname() == null)||user.getFirstname().isEmpty()){
            errors.rejectValue("firstname", "firstname.required", "First name must not be empty");
        } else if (user.getFirstname().length() > 45) {
            errors.rejectValue("firstname", "firstname.required", "First name must be less than 46 characters");
        }
        if((user.getLastname() == null)||user.getLastname().isEmpty()){
            errors.rejectValue("lastname", "lastname.required", "Last name must not be empty");
        } else if (user.getLastname().length() > 45) {
            errors.rejectValue("lastname", "lastname.required", "Last name must be less than 46 characters");
        }
        if (user.getMidname().length() > 45) {
            errors.rejectValue("midname", "midname.required", "Middle name must be less than 46 characters");
        }
    }
}
