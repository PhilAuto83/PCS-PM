package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.User;
import com.phildev.pcs.repositories.UserRepository;
import com.phildev.pcs.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public String home(Model model, Principal connectedUser)
    {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("connectedUser", connectedUser.getName());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(Model model, Principal connectedUser) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model, Principal connectedUser) {
        String passwordValidation = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$";
        model.addAttribute("connectedUser", connectedUser.getName());

        if (!result.hasErrors() && Pattern.matches(passwordValidation, user.getPassword())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.save(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }
        model.addAttribute("validationError", "Password must be at minimum 8 characters with 1 digit, 1 lowercase letter, " +
                "1 uppercase letter, 1 special character betweeen @$!%?&");
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        String passwordValidation = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$";
        if (result.hasErrors()|| Pattern.matches(passwordValidation, user.getPassword())) {
            model.addAttribute("validationError","Password must be at minimum 8 characters with 1 digit, 1 lowercase letter, " +
                    "1 uppercase letter, 1 special character betweeen @$!%?&");
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userService.save(user);
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userService.delete(user);
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }
}
