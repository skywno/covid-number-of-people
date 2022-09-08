package com.example.covid.controller.api;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class APIAuthController {

    @GetMapping("/sign-up")
    public String signUp(){
        return "done.";
    }

    @GetMapping("/login")
    public String login() {
        return "done.";
    }

}
