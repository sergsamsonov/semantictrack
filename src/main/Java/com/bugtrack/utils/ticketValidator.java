package com.bugtrack.utils;

import com.bugtrack.entity.ticket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * The ticketValidator provides methods
 * to validate ticket objects
 * @version 0.9.9 3 April 2016
 * @author  Sergey Samsonov
 */
@Component
public class ticketValidator implements Validator {
    @Override
    public boolean supports(Class<?> type) {
        return ticket.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ticket ticket = (ticket) o;
        if((ticket.getTickstat() == null)||(ticket.getTickstat() == 0)){
            errors.rejectValue("tickstat", "tickstat.required", "Ticket status must not be empty");
        }
        if ((ticket.getIssue() == null)||ticket.getIssue().isEmpty()) {
            errors.rejectValue("issue", "issue.required", "Issue must not be empty");
        } else if (ticket.getIssue().length() > 70) {
            errors.rejectValue("issue", "issue.required", "Issue must be less than 71 characters");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "issuedescr", "issuedescr.required",
                "Issuedescr must not be empty");
        if((ticket.getTicktype() == null)||(ticket.getTicktype() == 0)){
            errors.rejectValue("ticktype", "ticktype.required", "Ticket type must not be empty");
        }
        if((ticket.getTask() == null)||(ticket.getTask() == 0)){
            errors.rejectValue("task", "task.required", "Current task must not be empty");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "responsible", "responsible.required",
                "Responsible must not be empty");
        if(ticket.getNumber() != null) {
            Integer Status = ticket.getTickstat();
            if ((Status != null)&&(Status != 1)) {
                if ((ticket.getSolution() == null)||ticket.getSolution().isEmpty()) {
                    errors.rejectValue("solution", "solution.required", "Solution must not be empty");
                } else if (ticket.getSolution().length() > 70) {
                    errors.rejectValue("solution", "solution.required", "Solution must be less than 71 characters");
                }
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "solutiondet", "solutiondet.required",
                        "Solution details must not be empty");
            }
        }
    }
}
