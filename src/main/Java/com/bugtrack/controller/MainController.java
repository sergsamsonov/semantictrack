package com.bugtrack.controller;

import com.bugtrack.entity.*;
import com.bugtrack.service.acronymService;
import com.bugtrack.service.groupService;
import com.bugtrack.service.permissionService;
import com.bugtrack.service.systvarService;
import com.bugtrack.service.taskService;
import com.bugtrack.service.ticket.ticketService;
import com.bugtrack.service.ticketstatService;
import com.bugtrack.service.ticktypeService;
import com.bugtrack.service.userService;
import com.bugtrack.service.usrgroupService;
import com.bugtrack.service.grpermissService;
import com.bugtrack.utils.acronymValidator;
import com.bugtrack.utils.groupValidator;
import com.bugtrack.utils.passwchngValidator;
import com.bugtrack.utils.permissionValidator;
import com.bugtrack.utils.searchAttrValidator;
import com.bugtrack.utils.taskValidator;
import com.bugtrack.utils.ticketValidator;
import com.bugtrack.utils.ticketstatValidator;
import com.bugtrack.utils.ticktypeValidator;
import com.bugtrack.utils.userValidator;
import java.util.*;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import static java.util.stream.Collectors.toMap;

/**
 * The MainController provides access to the application behaviour
 * @version 0.9.9 31 July 2016
 * @author  Sergey Samsonov
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private acronymService acronymService;

    @Autowired
    private groupService groupService;

    @Autowired
    private grpermissService grperService;

    @Autowired
    private permissionService permissService;

    @Autowired
    private systvarService systvarService;

    @Autowired
    private taskService taskService;

    @Autowired
    private ticketService ticketService;

    @Autowired
    private ticketstatService tickstatService;

    @Autowired
    private ticktypeService ticktypeService;

    @Autowired
    private userService userService;

    @Autowired
    private usrgroupService usrgrService;

    @Autowired
    private acronymValidator acronymValidator;

    @Autowired
    private groupValidator groupValidator;

    @Autowired
    private passwchngValidator passwchngValidator;

    @Autowired
    private permissionValidator permissValidator;

    @Autowired
    private searchAttrValidator searchAttrValidator;

    @Autowired
    private taskValidator taskValidator;

    @Autowired
    private ticketValidator ticketValidator;

    @Autowired
    private ticketstatValidator tickstValidator;

    @Autowired
    private ticktypeValidator ticktypeValidator;

    @Autowired
    private userValidator userValidator;

    @RequestMapping(method = RequestMethod.GET)
    public String start() {
        return "redirect:/tickets/tasks";
    }

    @RequestMapping(value = "/tickets/tasks")
    public ModelAndView getTasks(Principal principal) {
        ModelAndView tasks = null;
        String tasksSum = "";
        user currentUser = (user) ((Authentication) principal).getPrincipal();
        if (currentUser != null) {
            List<ticket> ticketList = ticketService.getTicketsByResponsible(currentUser.getId());
            Integer tasksNum = ticketList.size();
            tasksSum = Integer.toString(tasksNum) + " current task"
                    + ((tasksNum > 0 || tasksNum == 0) ? "s" : "");
            tasks = new ModelAndView("/mytasks", "tasks", ticketList);
            tasks.addObject("taskssum", tasksSum);
        }
        return tasks;
    }

    @RequestMapping(value="/tickets/create", method=RequestMethod.GET)
    public String createTicket(@ModelAttribute("ticketAttribute") ticket ticket, Model model) {
        model.addAttribute("tasks", taskService.getAll());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("ticktypes", ticktypeService.getAll());
        model.addAttribute("tickstatus", tickstatService.getAll());
        return "ticketcrupd";
    }

    @RequestMapping(value="/tickets/update/{number}", method=RequestMethod.GET)
    public ModelAndView updateTicket(@PathVariable Integer number) {
        ModelAndView ticketUpd = null;
        if (number != null) {
            ticket ticket = ticketService.getTicketByNumber(number);
            ticketUpd = new ModelAndView("/ticketcrupd", "ticketAttribute", ticket);
            ticketUpd.addObject("tasks", taskService.getAll());
            ticketUpd.addObject("tickstatus", tickstatService.getAll());
            ticketUpd.addObject("ticktypes", ticktypeService.getAll());
            ticketUpd.addObject("users", userService.getAll());
        }
        return ticketUpd;
    }

    @RequestMapping(value="/tickets/save", method=RequestMethod.POST)
    public ModelAndView saveTicket(@ModelAttribute("ticketAttribute") ticket ticket, Principal principal,
                                   BindingResult result) {
        Integer ticketNumber = ticket.getNumber();
        ticketValidator.validate(ticket, result);
        if (!result.hasErrors()) {
            user currentUser = (user) ((Authentication) principal).getPrincipal();
            if (currentUser != null) {
                ticket.setUser(currentUser.getId());
            }
            ticketService.processTicket(ticket);
            if (ticketNumber == null) {
                this.ticketService.addEntity(ticket);
                systvar ticknum = systvarService.getSystVarByCode("ticknum");
                if (ticknum != null) {
                    ticknum.setValue(String.valueOf(Integer.parseInt(ticknum.getValue()) + 1));
                    systvarService.updateEntity(ticknum);
                }
                return new ModelAndView("redirect:/tickets/tasks");
            } else {
                this.ticketService.updateEntity(ticket);
                return showTicket(ticketNumber);
            }
        }
        ModelAndView ticketForm = new ModelAndView("/ticketcrupd", "ticketAttribute", ticket);
        ticketForm.addObject("tasks", taskService.getAll());
        ticketForm.addObject("ticktypes", ticktypeService.getAll());
        ticketForm.addObject("tickstatus", tickstatService.getAll());
        ticketForm.addObject("users", userService.getAll());
        return ticketForm;
    }

    @RequestMapping(value="/tickets/show/{number}", method=RequestMethod.GET)
    public ModelAndView showTicket(@PathVariable Integer number){
        ModelAndView ticketShow = null;
        if (number != null) {
            ticket ticket = ticketService.getTicketByNumber(number);
            ticketShow = new ModelAndView("/ticketshow", "ticketAttribute", ticket);
            ticketstat tickstat = new ticketstat();
            ticktype ticktype = new ticktype();
            task task = new task();
            user user = new user();
            if (ticket != null) {
                task = taskService.getTaskById(ticket.getTask());
                tickstat = tickstatService.getStatById(ticket.getTickstat());
                ticktype = ticktypeService.getTypeById(ticket.getTicktype());
                user = userService.getUserById(ticket.getResponsible());
            }
            ticketShow.addObject("ticktask", task);
            ticketShow.addObject("tickstat", tickstat);
            ticketShow.addObject("ticktype", ticktype);
            ticketShow.addObject("resp", user);
        }
        return ticketShow;
    }

    @RequestMapping(value="/tickets/ticketsProcessedCheck", method=RequestMethod.POST)
    public @ResponseBody String ticketsProcCheck(){
        String result = "";
        systvar ticknum = systvarService.getSystVarByCode("tickproc");
        if ((ticknum == null)||!ticknum.getValue().equals("YES")) {
            result = "We cannot initialize the operation, because tickets are not processed. " +
                    "Please run a processing tickets before the operation.";
        }
        return result;
    }

    @RequestMapping(value="/tickets/search/{userreq}", method=RequestMethod.GET)
    public String search(@PathVariable String userreq, Model model){
        String searchSum = "";
        ticket[] tickets = ticketService.getTickets(userreq, "", 1);
        Integer ticketsNum = (tickets == null)?0:tickets.length;
        searchSum = Integer.toString(ticketsNum) + " result"
                + ((ticketsNum > 0 || ticketsNum == 0) ? "s" : "") + " for " + "'" + userreq+ "'";
        model.addAttribute("searchsum", searchSum);
        if (tickets != null) {
            model.addAttribute("tickets", tickets);
        }
        return "search";
    }

    @RequestMapping(value="/tickets/detsearch", method=RequestMethod.GET)
    public String skipGramAttrInput(@ModelAttribute("searchAttr") searchAttributes searchAttributes, Model model) {
        searchAttributes.setSimfield("issue");
        model.addAttribute("searchAttr", searchAttributes);
        return "detsearch";
    }

    @RequestMapping(value = "/tickets/getsearchres", method=RequestMethod.POST)
    public ModelAndView getDetSearchRes(@ModelAttribute("searchAttr") searchAttributes searchAttr,
                                        BindingResult result) {
        ModelAndView detSearch = null;
        searchAttrValidator.validate(searchAttr, result);
        if (result.hasErrors()) {
            detSearch = new ModelAndView("/detsearch", "searchAttr", searchAttr);
        } else {
            String searchSum = "";
            ticket[] tickets = new ticket[0];
            tickets = ticketService.getTickets(searchAttr.getRequest(),
                    searchAttr.getSimfield(), 1);
            Integer ticketsNum = tickets.length;
            searchSum = Integer.toString(ticketsNum) + " result"
                    + ((ticketsNum > 0 || ticketsNum == 0) ? "s" : "") + " for "
                    + "'" + searchAttr.getRequest() + "'";
            detSearch = new ModelAndView("/detsearchres");
            detSearch.addObject("tickets", tickets);
            detSearch.addObject("searchsum", searchSum );
        }
        return detSearch;
    }

    @RequestMapping(value = "/acronyms/list")
    public ModelAndView getAcronyms() {
        List<acronym> acronymsList = acronymService.getAll();
        return new ModelAndView("/acronyms", "acronyms", acronymsList);
    }

    @RequestMapping(value="/acronyms/create", method=RequestMethod.GET)
    public String createAcronym(@ModelAttribute("acronymAttribute") acronym acronym) {
        return "acronymform";
    }

    @RequestMapping(value="/acronyms/update/{id}", method=RequestMethod.GET)
    public ModelAndView updateAcronym(@PathVariable Integer id){
        ModelAndView acronymsUpd = null;
        if (id != null) {
            acronym acronym = acronymService.getAcronymById(id);
            acronym.setOldacronym(acronym.getAcronym());
            acronymsUpd = new ModelAndView("/acronymform", "acronymAttribute", acronym);
        }
        return acronymsUpd;
    }

    @RequestMapping(value="/acronyms/delete/{id}", method=RequestMethod.GET)
    public ModelAndView deleteAcronym(@PathVariable Integer id){
        ModelAndView acronymsUpd = null;
        if (id != null) {
            acronym acronym = acronymService.getAcronymById(id);
            this.acronymService.delEntity(acronym);
        }
        return getAcronyms();
    }

    @RequestMapping(value="/acronyms/save", method=RequestMethod.POST)
    public ModelAndView saveAcronym(@ModelAttribute("acronymAttribute") acronym acronym, BindingResult result) {
        Integer acronymId = acronym.getId();
        acronymValidator.validate(acronym, result);
        if (!result.hasErrors()) {
            if (acronymId == null) {
                this.acronymService.addEntity(acronym);
            } else {
                this.acronymService.updateEntity(acronym);
            }
            return getAcronyms();
        }
        return new ModelAndView("/acronymform", "acronymAttribute", acronym);
    }

    @RequestMapping(value="/admin/users/list", method=RequestMethod.GET)
    public ModelAndView getUsersList() {
        List<user> usersList = userService.getAll();
        usersList.stream()
                 .forEach(u -> {u.setGrnames(usrgrService.getGrNamesByUserId(u.getId()));});
        return new ModelAndView("/users", "users", usersList);
    }

    @RequestMapping(value="/admin/users/create", method=RequestMethod.GET)
    public String createUser(@ModelAttribute("userAttribute") user user, Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "userform";
    }

    @RequestMapping(value="/admin/users/update/{id}", method=RequestMethod.GET)
    public ModelAndView updateUser(@PathVariable Integer id){
        ModelAndView userUpd = null;
        if (id != null) {
            user user = userService.getUserById(id);
            user.setOldlogin(user.getLogin());
            user.setGrlist(usrgrService.getGrListByUserId(id));
            userUpd = new ModelAndView("/userform", "userAttribute", user);
            userUpd.addObject("groups", groupService.getAll());
        }
        return userUpd;
    }

    @RequestMapping(value="/admin/users/delete/{id}", method=RequestMethod.GET)
    public ModelAndView deleteUser(@PathVariable Integer id){
        if (id != null) {
            usrgrService.delUsrgroupsByUserId(id);
            user user = userService.getUserById(id);
            this.userService.delEntity(user);
        }
        return getUsersList();
    }

    @RequestMapping(value="/admin/users/save", method=RequestMethod.POST)
    public ModelAndView saveUser(@ModelAttribute("userAttribute") user user, BindingResult result) {
        Integer userId = user.getId();
        userValidator.validate(user, result);
        if (!result.hasErrors()) {
            if (userId == null) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                this.userService.addEntity(user);
            } else {
                this.userService.updateEntity(user);
                usrgrService.delUsrgroupsByUserId(userId);
            }
            List<String> Grlist = user.getGrlist();
            if (Grlist != null) {
                Grlist.stream()
                      .forEach(gr -> {
                          usrgrService.addEntity(new usrgroup(user.getId(), Integer.parseInt(gr)));
                      });
            }
            return getUsersList();
        }
        ModelAndView userForm = new ModelAndView("/userform", "userAttribute", user);
        userForm.addObject("groups", groupService.getAll());
        return userForm;
    }

    @RequestMapping(value="/profile/passwchng", method=RequestMethod.GET)
    public String changePassword(@ModelAttribute("passwchngAttr") passwordChange passwordChange, Model model) {
        model.addAttribute("passwchngAttr", passwordChange);
        return "passwchange";
    }

    @RequestMapping(value = "/profile/setpassword", method=RequestMethod.POST)
    public String setPassword(@ModelAttribute("passwchngAttr") passwordChange passwChange,
                              Model model, Principal principal, BindingResult result) {
        String passwChng = null;
        user currentUser = (user) ((Authentication) principal).getPrincipal();
        if (currentUser != null) {
            passwChange.setRightPassword(currentUser.getPassword());
            passwchngValidator.validate(passwChange, result);
            if (!result.hasErrors()) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(passwChange.getPassword());
                currentUser.setPassword(encodedPassword);
                this.userService.updateEntity(currentUser);
                passwChng = "passwchngmes";
            } else {
                model.addAttribute("passwchngAttr", passwChange);
                passwChng = "passwchange";
            }
        }
        return passwChng;
    }

    @RequestMapping(value="/admin/groups/list", method=RequestMethod.GET)
    public ModelAndView getGroupsList() {
        List<group> groupsList = groupService.getAll();
        groupsList.stream()
                  .forEach(gr -> {gr.setPernames(grperService.getPerNamesByGroupId(gr.getId()));});
        return new ModelAndView("/groups", "groups", groupsList);
    }

    @RequestMapping(value="/admin/groups/create", method=RequestMethod.GET)
    public String createGroup(@ModelAttribute("groupAttribute") group group, Model model) {
        model.addAttribute("permissions", permissService.getAll());
        return "groupform";
    }

    @RequestMapping(value="/admin/groups/update/{id}", method=RequestMethod.GET)
    public ModelAndView updateGroup(@PathVariable Integer id){
        ModelAndView groupUpd = null;
        if (id != null) {
            group group = groupService.getGroupById(id);
            group.setOldname(group.getName());
            group.setPerlist(grperService.getPerListByGroupId(id));
            groupUpd = new ModelAndView("/groupform", "groupAttribute", group);
            groupUpd.addObject("permissions", permissService.getAll());
        }
        return groupUpd;
    }

    @RequestMapping(value="/admin/groups/delete/{id}", method=RequestMethod.GET)
    public ModelAndView deleteGroup(@PathVariable Integer id){
        if (id != null) {
            grperService.delGrpermissByGroupId(id);
            group group = groupService.getGroupById(id);
            this.groupService.delEntity(group);
        }
        return getGroupsList();
    }

    @RequestMapping(value="/admin/groups/save", method=RequestMethod.POST)
    public ModelAndView saveGroup(@ModelAttribute("groupAttribute") group group, BindingResult result) {
        Integer groupId = group.getId();
        groupValidator.validate(group, result);
        if (!result.hasErrors()) {
            if (groupId == null) {
                this.groupService.addEntity(group);
            } else {
                this.groupService.updateEntity(group);
                grperService.delGrpermissByGroupId(groupId);
            }
            List<String> Perlist = group.getPerlist();
            if (Perlist != null) {
                Perlist.stream()
                       .forEach(p -> {
                           grperService.addEntity(new grpermiss(group.getId(), Integer.parseInt(p)));
                       });
            }
            return getGroupsList();
        }
        ModelAndView groupForm = new ModelAndView("/groupform", "groupAttribute", group);
        groupForm.addObject("permissions", permissService.getAll());
        return groupForm;
    }

    @RequestMapping(value="/admin/groups/groupUsageCheck", method=RequestMethod.POST)
    public @ResponseBody String groupUsageCheck(@RequestParam(value = "groupid", required = true) Integer groupId,
                                                @RequestParam(value = "groupname", defaultValue = "")
                                                String groupName){
        String result = "";
        if(groupId.intValue() != 0){
            group group = groupService.getGroupById(groupId);
            if (group == null) {
                result = "The group " + groupName + " has not been added to the list.";
            } else if (usrgrService.groupUsageCheck(groupId)) {
                result = "The group " + groupName
                        + " cannot be deleted, because the group is used by users.";
            }
        } else {
            result = "The group " + groupName + " has not been added to the list.";
        }
        return result;
    }

    @RequestMapping(value="/admin/permissions/list", method=RequestMethod.GET)
    public ModelAndView getPermissList() {
        List<permission> permissList = permissService.getAll();
        return new ModelAndView("/permissions", "permissions", permissList);
    }

    @RequestMapping(value="/admin/permissions/create", method=RequestMethod.GET)
    public String createPermiss(@ModelAttribute("permissAttribute") permission permission) {
        return "permissform";
    }

    @RequestMapping(value="/admin/permissions/update/{id}", method=RequestMethod.GET)
    public ModelAndView updatePermiss(@PathVariable Integer id){
        ModelAndView permissUpd = null;
        if (id != null) {
            permission permission = permissService.getPermissById(id);
            permission.setOldname(permission.getName());
            permissUpd = new ModelAndView("/permissform", "permissAttribute", permission);
        }
        return permissUpd;
    }

    @RequestMapping(value="/admin/permissions/delete/{id}", method=RequestMethod.GET)
    public ModelAndView deletePermiss(@PathVariable Integer id, Model model){
        if (id != null) {
            permission permission = permissService.getPermissById(id);
            this.permissService.delEntity(permission);
        }
        return getPermissList();
    }

    @RequestMapping(value="/admin/permissions/save", method=RequestMethod.POST)
    public ModelAndView savePermiss(@ModelAttribute("permissAttribute") permission permission, BindingResult result) {
        Integer permissId = permission.getId();
        permissValidator.validate(permission, result);
        if (!result.hasErrors()) {
            if (permissId == null) {
                this.permissService.addEntity(permission);
            } else {
                this.permissService.updateEntity(permission);
            }
            return getPermissList();
        }
        return new ModelAndView("/permissform", "permissAttribute", permission);
    }

    @RequestMapping(value="/admin/permissions/permissUsageCheck", method=RequestMethod.POST)
    public @ResponseBody String permissUsageCheck(
            @RequestParam(value = "permissid", required = true) Integer permissId,
            @RequestParam(value = "permissname", defaultValue = "") String permissName){
        String result = "";
        if(permissId.intValue() != 0){
            permission permission = permissService.getPermissById(permissId);
            if (permission == null) {
                result = "The permission " + permissName + " has not been added to the list.";
            } else if (grperService.permissUsageCheck(permissId)) {
                result = "The permission '" + permissName
                        + "' cannot be deleted, because the permission is used.";
            }
        } else {
            result = "The permission " + permissName + " has not been added to the list.";
        }
        return result;
    }

    @RequestMapping(value="/admin/ticketstat/list", method=RequestMethod.GET)
    public ModelAndView getTicketStatList() {
        List<ticketstat> tickstList = tickstatService.getAll();
        return new ModelAndView("/tickstatus", "tickstat", tickstList);
    }

    @RequestMapping(value="/admin/ticketstat/create", method=RequestMethod.GET)
    public String createTicketStat(@ModelAttribute("tickstatAttribute") ticketstat ticketstat) {
        return "tickstform";
    }

    @RequestMapping(value="/admin/ticketstat/update/{id}", method=RequestMethod.GET)
    public ModelAndView updateTicketStat(@PathVariable Integer id){
        ModelAndView tickstUpd = null;
        if (id != null) {
            ticketstat tickstat = tickstatService.getStatById(id);
            tickstat.setOldname(tickstat.getName());
            tickstUpd = new ModelAndView("/tickstform", "tickstatAttribute", tickstat);
        }
        return tickstUpd;
    }

    @RequestMapping(value="/admin/ticketstat/delete/{id}", method=RequestMethod.GET)
    public ModelAndView deleteTicketStat(@PathVariable Integer id){
        if (id != null) {
            ticketstat tickstat = tickstatService.getStatById(id);
            this.tickstatService.delEntity(tickstat);
        }
        return getTicketStatList();
    }

    @RequestMapping(value="/admin/ticketstat/save", method=RequestMethod.POST)
    public ModelAndView saveTicketStat(@ModelAttribute("tickstatAttribute") ticketstat ticketstat,
                                       BindingResult result) {
        Integer tickstId = ticketstat.getId();
        tickstValidator.validate(ticketstat, result);
        if (!result.hasErrors()) {
            if (tickstId == null) {
                this.tickstatService.addEntity(ticketstat);
            } else {
                this.tickstatService.updateEntity(ticketstat);
            }
            return getTicketStatList();
        }
        return new ModelAndView("/tickstform", "tickstatAttribute", ticketstat);
    }

    @RequestMapping(value="/admin/ticktypes/list", method=RequestMethod.GET)
    public ModelAndView getTickTypesList() {
        List<ticktype> typesList = ticktypeService.getAll();
        return new ModelAndView("/ticktypes", "ticktypes", typesList);
    }

    @RequestMapping(value="/admin/ticktypes/create", method=RequestMethod.GET)
    public String createTicketType(@ModelAttribute("ticktypeAttribute") ticktype ticktype) {
        return "ticktypeform";
    }

    @RequestMapping(value="/admin/ticktypes/update/{id}", method=RequestMethod.GET)
    public ModelAndView updateTicketType(@PathVariable Integer id){
        ModelAndView typeUpd = null;
        if (id != null) {
            ticktype ticktype = ticktypeService.getTypeById(id);
            ticktype.setOldname(ticktype.getName());
            typeUpd = new ModelAndView("/ticktypeform", "ticktypeAttribute", ticktype);
        }
        return typeUpd;
    }

    @RequestMapping(value="/admin/ticktypes/delete/{id}", method=RequestMethod.GET)
    public ModelAndView deleteTicketType(@PathVariable Integer id){
        if (id != null) {
            ticktype ticktype = ticktypeService.getTypeById(id);
            this.ticktypeService.delEntity(ticktype);
        }
        return getTickTypesList();
    }

    @RequestMapping(value="/admin/ticktypes/save", method=RequestMethod.POST)
    public ModelAndView saveTicketType(@ModelAttribute("ticktypeAttribute") ticktype ticktype, BindingResult result) {
        Integer typeId = ticktype.getId();
        ticktypeValidator.validate(ticktype, result);
        if (!result.hasErrors()) {
            if (typeId == null) {
                this.ticktypeService.addEntity(ticktype);
            } else {
                this.ticktypeService.updateEntity(ticktype);
            }
            return getTickTypesList();
        }
        return new ModelAndView("/ticktypeform", "ticktypeAttribute", ticktype);
    }

    @RequestMapping(value="/admin/tasks/list", method=RequestMethod.GET)
    public ModelAndView getTasksList() {
        List<task> tasksList = taskService.getAll();
        return new ModelAndView("/tasks", "tasks", tasksList);
    }

    @RequestMapping(value="/admin/tasks/create", method=RequestMethod.GET)
    public String createTask(@ModelAttribute("taskAttribute") task task) {
        return "/taskform";
    }

    @RequestMapping(value="/admin/tasks/update/{id}", method=RequestMethod.GET)
    public ModelAndView updateTask(@PathVariable Integer id){
        ModelAndView taskUpd = null;
        if (id != null) {
            task task = taskService.getTaskById(id);
            task.setOldname(task.getName());
            taskUpd = new ModelAndView("/taskform", "taskAttribute", task);
        }
        return taskUpd;
    }

    @RequestMapping(value="/admin/tasks/delete/{id}", method=RequestMethod.GET)
    public ModelAndView deleteTask(@PathVariable Integer id){
        if (id != null) {
            task task = taskService.getTaskById(id);
            this.taskService.delEntity(task);
        }
        return getTasksList();
    }

    @RequestMapping(value="/admin/tasks/save", method=RequestMethod.POST)
    public ModelAndView saveTask(@ModelAttribute("taskAttribute") task task, BindingResult result) {
        taskValidator.validate(task, result);
        if (!result.hasErrors()) {
            if (task.getId() == null) {
                this.taskService.addEntity(task);
            } else {
                this.taskService.updateEntity(task);
            }
            return getTasksList();
        }
        return new ModelAndView("/taskform", "taskAttribute", task);
    }

    @RequestMapping(value="/admin/processtick", method=RequestMethod.GET)
    public String processTickets() {
        ticketService.processAllTickets();
        return "tickprocsucc";
    }

    @RequestMapping(value="/help", method=RequestMethod.GET)
    public String getHelpPage() {
        return "help";
    }

    @RequestMapping(value="/err403", method=RequestMethod.GET)
    public String getErr403Page() {
        return "err403";
    }

}
