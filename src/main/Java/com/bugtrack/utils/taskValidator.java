package com.bugtrack.utils;

import com.bugtrack.entity.task;
import com.bugtrack.service.taskService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * The tasksValidator provides methods
 * to validate tasks objects
 * @version 0.9.9 31 July 2016
 * @author  Sergey Samsonov
 */
@Component
public class taskValidator implements Validator {
    @Override
    public boolean supports(Class<?> type) {
        return task.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        task task = (task) o;
        if((task.getName() == null)||task.getName().isEmpty()){
            errors.rejectValue("name", "name.required", "Name must not be empty");
        } else if (task.getName().length() > 15) {
            errors.rejectValue("name", "name.required", "Name must be less than 16 characters");
        } else {
            String taskName = task.getName().trim();
            taskService taskService = new taskService();
            task taskByName = taskService.getTaskByName(taskName);
            if((taskByName != null)&& ((task.getId() == null)||(task.getId() != taskByName.getId()))){
                errors.rejectValue("name", "name.required",
                        "Task with name " + taskByName + " already exists. Please enter other name.");
            }
        }
        if (task.getDescription().length() > 45) {
            errors.rejectValue("description", "description.required", "Description must be less than 46 characters");
        }
    }
}
