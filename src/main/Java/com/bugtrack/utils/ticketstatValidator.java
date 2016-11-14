package com.bugtrack.utils;

import com.bugtrack.entity.ticketstat;
import com.bugtrack.service.ticketstatService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * The tickstatValidator provides methods
 * to validate ticketstat objects
 * @version 0.9.9 7 July 2016
 * @author  Sergey Samsonov
 */
@Component
public class ticketstatValidator implements Validator {
    @Override
    public boolean supports(Class<?> type) {
        return ticketstat.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ticketstat ticketstat = (ticketstat) o;
        if((ticketstat.getName() == null)||ticketstat.getName().isEmpty()){
            errors.rejectValue("name", "name.required", "Name must not be empty");
        } else if (ticketstat.getName().length() > 15) {
            errors.rejectValue("name", "name.required", "Name must be less than 16 characters");
        } else {
            String tickstName = ticketstat.getName().trim();
            ticketstatService tickstatService = new ticketstatService();
            ticketstat tickstByName = tickstatService.getStatByName(tickstName);
            if((tickstByName != null)&&
               ((ticketstat.getId() == null)||(ticketstat.getId() != tickstByName.getId()))){
                errors.rejectValue("name", "name.required",
                        "Ticket status with name " + tickstByName + " already exists. Please enter other name.");
            }
        }
        if (ticketstat.getDescription().length() > 45) {
            errors.rejectValue("description", "description.required", "Description must be less than 46 characters");
        }
    }
}
