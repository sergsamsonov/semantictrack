package com.bugtrack.utils;

import com.bugtrack.entity.searchAttributes;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The acronymValidator provides methods
 * to validate searchAttributes objects
 * @version 0.9.9 3 April 2016
 * @author  Sergey Samsonov
 */
@Component
public class searchAttrValidator implements Validator{

    @Override
    public boolean supports(Class<?> type) {
        return searchAttributes.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        searchAttributes searchAttributes = (searchAttributes) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "request", "request.required",
                "Request must not be empty");
    }

}
