package com.bugtrack.utils;

import com.bugtrack.entity.group;
import com.bugtrack.service.groupService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * The groupsValidator provides methods
 * to validate groups objects
 * @version 0.9.9 5 July 2016
 * @author  Sergey Samsonov
 */
@Component
public class groupValidator implements Validator {
    @Override
    public boolean supports(Class<?> type) {
        return group.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        group group = (group) o;
        if((group.getName() == null)||group.getName().isEmpty()){
            errors.rejectValue("name", "name.required", "Name must not be empty");
        } else if (group.getName().length() > 10) {
            errors.rejectValue("name", "name.required", "Name must be less than 11 characters");
        } else {
            String grName = group.getName().trim();
            groupService groupService = new groupService();
            group groupByName = groupService.getGroupByName(grName);
            if((groupByName != null)&&((group.getId() == null)||(group.getId() != groupByName.getId()))){
                errors.rejectValue("name", "name.required",
                        "Group with name " + grName + " already exists. Please enter other name.");
            }
        }
        if (group.getDescription().length() > 45) {
            errors.rejectValue("description", "description.required", "Description must be less than 46 characters");
        }
    }
}
