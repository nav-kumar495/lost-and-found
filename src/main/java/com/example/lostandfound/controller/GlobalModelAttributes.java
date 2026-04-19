package com.example.lostandfound.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("currentUser")
    public String populateCurrentUser(HttpSession session) {
        return (String) session.getAttribute("loggedInUser");
    }
}
