package com.bugtrack.utils;

import com.bugtrack.entity.permission;
import com.bugtrack.service.permissionService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * The permissionValidator provides methods
 * to validate groups objects
 * @version 0.9.9 6 July 2016
 * @author  Sergey Samsonov
 */
@Component
public class permissionValidator implements Validator {
    @Override
    public boolean supports(Class<?> type) {
        return permission.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        permission permission = (permission) o;

        if((permission.getName() == null)||permission.getName().trim().isEmpty()){
            errors.rejectValue("name", "name.required", "Name must not be empty");
        } else if (permission.getName().length() > 10) {
            errors.rejectValue("name", "name.required", "Name must be less than 11 characters");
        } else {
            String perName = permission.getName().trim();
            permissionService permissService = new permissionService();
            permission permissByName = permissService.getPermissByName(perName);
            if((permissByName != null)&&
               ((permission.getId() == null)||(permission.getId() != permissByName.getId()))){
                errors.rejectValue("name", "name.required",
                        "Permission with name " + perName + " already exists. Please enter other name.");
            }
        }
        if (permission.getDescription().length() > 45) {
            errors.rejectValue("description", "description.required", "Description must be less than 46 characters");
        }
    }
}
