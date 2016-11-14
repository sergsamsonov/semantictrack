package com.bugtrack.utils;

import com.bugtrack.entity.ticktype;
import com.bugtrack.service.ticktypeService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * The ticktypeValidator provides methods
 * to validate ticktype objects
 * @version 0.9.9 7 July 2016
 * @author  Sergey Samsonov
 */
@Component
public class ticktypeValidator implements Validator {
    @Override
    public boolean supports(Class<?> type) {
        return ticktype.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ticktype ticktype = (ticktype) o;
        if((ticktype.getName() == null)||ticktype.getName().isEmpty()){
            errors.rejectValue("name", "name.required", "Name must not be empty");
        } else if (ticktype.getName().length() > 15) {
            errors.rejectValue("name", "name.required", "Name must be less than 16 characters");
        } else {
            String typeName = ticktype.getName().trim();
            ticktypeService ticktypeService = new ticktypeService();
            ticktype typeByName = ticktypeService.getTypeByName(typeName);
            if((typeByName != null)&&
               ((ticktype.getId() == null)||(ticktype.getId() != typeByName.getId()))){
                errors.rejectValue("name", "name.required",
                        "Ticket type with name " + typeByName + " already exists. Please enter other name.");
            }
        }
        if (ticktype.getDescription().length() > 45) {
            errors.rejectValue("description", "description.required", "Description must be less than 46 characters");
        }
    }
}
