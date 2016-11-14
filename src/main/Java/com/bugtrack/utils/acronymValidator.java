package com.bugtrack.utils;

import com.bugtrack.entity.acronym;
import com.bugtrack.service.acronymService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The acronymValidator provides methods
 * to validate acronyms objects
 * @version 0.9.9 3 April 2016
 * @author  Sergey Samsonov
 */
@Component
public class acronymValidator implements Validator {
    @Override
    public boolean supports(Class<?> type) {
        return acronym.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        acronym acronym = (acronym) o;
        if((acronym.getAcronym() == null)||acronym.getAcronym().isEmpty()){
            errors.rejectValue("acronym", "acronym.required", "Acronym must not be empty");
        } else if (acronym.getAcronym().length() > 10) {
            errors.rejectValue("acronym", "acronym.required", "Acronym must be less than 11 characters");
        } else {
            acronymService acronymService = new acronymService();
            String acron = acronym.getAcronym();
            acronym acronByName = acronymService.getAcronymByName(acron);
            if((acronByName != null)&&
               ((acronym.getId() == null)||(acronym.getId() != acronByName.getId()))){
                errors.rejectValue("acronym", "acronym.required",
                        "Acronym " + acron + " exists. Please enter other value.");
            }
        }
        if((acronym.getInterpret() == null)||acronym.getInterpret().isEmpty()){
            errors.rejectValue("interpret", "interpret.required", "Interpretation must not be empty");
        } else if (acronym.getInterpret().length() > 50) {
            errors.rejectValue("interpret", "interpret.required", "Interpretation must be less than 51 characters");
        }
    }
}

