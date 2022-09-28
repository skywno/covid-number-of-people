package com.example.covid.controller.api;


import com.example.covid.dto.AdminRequest;
import com.example.covid.dto.APIDataResponse;
import com.example.covid.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api")
public class APIAuthController {

    @PostMapping("/sign-up")
    public APIDataResponse<String> signUp(@RequestBody AdminRequest adminRequest){
        return APIDataResponse.empty();
    }

    @PostMapping("/login")
    public APIDataResponse<String> login(@RequestBody LoginRequest loginRequest) {
        return APIDataResponse.empty();
    }

}
