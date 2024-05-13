package com.ai.calorieTrackerApp.controller;

import com.ai.calorieTrackerApp.models.userDetails;
import com.ai.calorieTrackerApp.models.userModel;
import com.ai.calorieTrackerApp.service.userDetailsService;
import com.ai.calorieTrackerApp.service.userModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class userDetailsController {

    private final userDetailsService service;

    @Autowired
    public userDetailsController(userDetailsService service) {
        this.service = service;
    }

    @PostMapping("/createUserDetails/{userId}")
    public userDetails createUserDetails(@RequestBody userDetails details, @PathVariable Long userId) {
//        System.out.println(details.getUserName());
        return service.createUserDetails(userId,details);
    }


    @PutMapping("/updateUserDetails/{userId}")
    public userDetails updateUserDetails(@PathVariable Long userId, @RequestBody userDetails details) {
//        System.out.println(userId);
        return service.updateUserDetails(userId,details);
    }

    @GetMapping("/getUserDetails/{userId}")
    public userDetails getUserDetails(@PathVariable Long userId){
            return service.getUserDetails(userId);
    }


}
