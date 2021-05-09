package com.mironov.image.studio.rest.controllers;

import com.mironov.image.studio.api.dto.UserUpdateDto;
import com.mironov.image.studio.api.services.ISecurityService;
import com.mironov.image.studio.api.services.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/settings")
public class SettingController {

    private static final String CURRENT_USER = "currentUser";
    private static final String SETTINGS = "settings";
    private static final String USER = "user";

    private final IUserService userService;
    private final ISecurityService securityService;

    public SettingController(IUserService userService, ISecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping
    public String settingsPage(Model model) {
        model.addAttribute(CURRENT_USER, this.securityService.findLoggedInUser());
        model.addAttribute(USER, this.userService.getUser(this.securityService.findLoggedInUser().getId()));
        return SETTINGS;
    }

    @PostMapping
    public String saveSettings(@ModelAttribute(USER) @Valid UserUpdateDto userUpdateDto, BindingResult bindingResult, Model model) {
        if (this.userService.findUserByName(userUpdateDto.getUsername()) &&
                !this.securityService.findLoggedInUser().getUsername().equals(userUpdateDto.getUsername())) {
            bindingResult.rejectValue("username", "username", "Этот логин уже занят!");
        }
        if (this.userService.findUserByEmail(userUpdateDto.getEmail()) &&
                !this.securityService.findLoggedInUser().getEmail().equals(userUpdateDto.getEmail())) {
            bindingResult.rejectValue("email", "email", "Эта электронная почта уже занята!");
        }
        if (this.userService.findUserByNumberPhone(userUpdateDto.getPhone()) &&
                this.securityService.findLoggedInUser().getNumberPhone() != userUpdateDto.getPhone()) {
            bindingResult.rejectValue("phone", "phone", "Этот номер телефона уже занят!");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute(CURRENT_USER, this.securityService.findLoggedInUser());
            model.addAttribute(USER, userUpdateDto);
            return SETTINGS;
        }
        this.userService.updateUser(userUpdateDto);
        return "redirect:/settings";
    }

}
