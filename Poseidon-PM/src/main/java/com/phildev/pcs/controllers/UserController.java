package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.domain.User;
import com.phildev.pcs.service.TradeService;
import com.phildev.pcs.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.regex.Pattern;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * This method is displaying user/list view when calling endpoint /user/list
     * @param model which is sending user connected and trades to the view to be displayed
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return user/list view
     */
    @RequestMapping("/user/list")
    public String home(Model model, Principal connectedUser)
    {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("connectedUser", connectedUser.getName());
        return "user/list";
    }

    /**
     * This method is displaying user/add view when calling endpoint /user/add
     * @param model which is sending user connected and  a single {@link User} object to the view to be used in the form
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return user/add view
     */
    @GetMapping("/user/add")
    public String addUser(Model model, Principal connectedUser) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "user/add";
    }


    /**
     * This method is validating {@link User} object and saving it in database through {@link UserService#save(User)}
     * @param user which is a {@link User} object
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return user/add if there is an error or redirect to /user/list endpoint to display new user list with freshly added user
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model, Principal connectedUser) {
        //adding regexp to validate password
        String passwordValidation = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$";
        model.addAttribute("connectedUser", connectedUser.getName());
        if (!result.hasErrors() && Pattern.matches(passwordValidation, user.getPassword())
                && !userService.checkUserNameOrFullNameExist(user)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.save(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        }else if(userService.checkUserNameOrFullNameExist(user)){
            model.addAttribute("userError", "User already exists with username or fullname");
            return "user/add";
        }else{
            model.addAttribute("validationError", "Password must be at minimum 8 characters with 1 digit, 1 lowercase letter, " +
                    "1 uppercase letter, 1 special character between @$!%?&");
            return "user/add";
        }
    }

    /**
     * This method is displaying user/update view with a user retrieved by its id through {@link UserService#findById(Integer)}
     * @param id which is the path variable to retrieve user by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return user/update view
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * This method is used to save a user which has been updated and retrieved by its id
     * @param id which is the path variable to retrieve user by id
     * @param user which is the {@link User} object updated by user in the form
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @return the user/add view if there is an error or the user/list if the update is successful
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        String passwordValidation = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$";
        if (result.hasErrors()|| !Pattern.matches(passwordValidation, user.getPassword())) {
            model.addAttribute("validationError","Password must be at minimum 8 characters with 1 digit, 1 lowercase letter, " +
                    "1 uppercase letter, 1 special character between @$!%?&");
            return "user/update";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userService.save(user);
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }

    /**
     * This method is deleting a user by its id by using the service {@link UserService#delete(Integer)}
     * @param id which is the path variable to retrieve user by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @return the user/list view after calling /user/list endpoint
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userService.delete(user);
        model.addAttribute("users", userService.findAll());
        return "redirect:/user/list";
    }
}
