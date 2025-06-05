package com.ikhzan.shm.web;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    /**
     * this is general page that does not need authentication
     */

    @GetMapping("/")
    public String home(Model model){
        try {
            model.addAttribute("welcomeText","welcome to structural health monitoring system");
            model.addAttribute("owner","isan");
            model.addAttribute("title","web spring");

            model.addAttribute("description","welcome to structural health monitoring system");
            return "home";
        }catch (Exception ex){
            model.addAttribute("error","Something wrong " + ex.getMessage());
            return "error";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "message", required = false) String message) {
        // You can add the error message to the model if you want to display it on the login page
        if (error != null) {
            // Handle the error message
            // You can use a model attribute to pass the message to the view
            // e.g., model.addAttribute("errorMessage", message);
        }
        return "login"; // This should return the name of your login view (e.g., login.html)
    }

    @GetMapping("/about")
    public String about(Model model){
        try {
            model.addAttribute("welcomeText","welcome to structural health monitoring system");
            model.addAttribute("owner","isan");
            model.addAttribute("title","web spring");

            model.addAttribute("description","welcome to structural health monitoring system");
            return "about";
        }catch (Exception ex){
            model.addAttribute("error","Something wrong " + ex.getMessage());
            return "error";
        }
    }

    @GetMapping("/error")
    public String error(Model model){
        model.addAttribute("error","Something went wrong");
        return "error";
    }

}
