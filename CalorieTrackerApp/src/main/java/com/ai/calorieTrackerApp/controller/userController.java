package com.ai.calorieTrackerApp.controller;

import com.ai.calorieTrackerApp.models.userDetails;
import com.ai.calorieTrackerApp.models.userModel;
import com.ai.calorieTrackerApp.service.userModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class userController {

    private final userModelService service;

    @Autowired
    public userController(userModelService service) {
        this.service = service;
    }

    @PostMapping("/createUser")
    public userModel createUser(@RequestBody userModel user) {
        System.out.println(user.getUserName());
        return service.createUser(user);
    }
    @GetMapping("/getUser/{userId}")
    public userModel getuserDetails(@PathVariable Long userId){
        return service.getUser(userId);
    }

    @GetMapping("/getUserByEmail/{emailId}")
    public userModel getuserDetailsByEmail(@PathVariable String emailId){
        return service.getUserByEmail(emailId);
    }

}
