package com.launchcode.myProject.controllers;

import com.launchcode.myProject.data.UserRepository;
import com.launchcode.myProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("users")
public class UserController {

    //spring will manage this for us
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String displayAllUsers(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute(new User());
        model.addAttribute("users", userRepository.findAll());
        if(id == null){
            model.addAttribute("title", "All Users");
            model.addAttribute("users", userRepository.findAll());
        } else {
            Optional<User> result = userRepository.findById(id);
            if (result.isEmpty()){
                model.addAttribute("title", "Invalid User ID: " + id );
            } else {
                User user = result.get();
                model.addAttribute("title", "Users: " + user.getName());
                model.addAttribute("users", user.getName());
            }
        }

        return "users/index";
    }

    @PostMapping
    public String processDeleteUserForm(@RequestParam(required = false) int[] userIds, Model model) {

        model.addAttribute("users", userRepository.findAll());
        long repoSize = userRepository.count();
         if(userIds == null) {
             model.addAttribute("title", "All Users");
             if(repoSize==0){
                 model.addAttribute("deleteError", "No users to delete!");
             }else{
                 model.addAttribute("deleteError", "Choose at least one user to delete.");
             }
         }else {
             for (int userId : userIds) {
                 userRepository.deleteById(userId);
                 return "redirect:/users";
             }
         }

         return "users/index";

    }

    @GetMapping("add")
    public String displayAddUserForm(Model model) {
        model.addAttribute("title", "Add User");
        model.addAttribute(new User());
        model.addAttribute("users", userRepository.findAll());
        return "users/add";
    }

    @PostMapping("add")
    public String processAddUserForm(@Valid @ModelAttribute User newUser,
                                         Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add User");
            return "users/add";
        }

        userRepository.save(newUser);
        return "redirect:";
    }

}
