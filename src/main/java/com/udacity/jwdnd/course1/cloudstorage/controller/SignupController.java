package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/signup")
public class SignupController {
  private final UserService userService;
    public SignupController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String signup(){
       return "signup";
    }

    @PostMapping
    public RedirectView signup(@ModelAttribute User user, Errors errors, Model model, RedirectAttributes attributes){
        boolean user1 = userService.isUserExist(user.getUsername());
        if (!user1){
            RedirectView redirectView = new RedirectView("/signup", true);
            attributes.addFlashAttribute("errorMessage", "Username already exist!");
            return redirectView;
        } else if ((userService.addUser(user)) < 0) {
            model.addAttribute("errorMessage, Something wrong, plz try to signup again.");
            RedirectView redirectView = new RedirectView("/signup", true);
            attributes.addFlashAttribute("errorMessage, Something wrong, plz try to signup again.");
            return redirectView;
        }else {
            RedirectView redirectView = new RedirectView("/login",true);
            attributes.addFlashAttribute("signupSuccess", true);
            return redirectView;
        }
    }
}
