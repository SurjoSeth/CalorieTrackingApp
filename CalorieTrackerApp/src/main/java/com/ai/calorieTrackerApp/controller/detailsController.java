package com.ai.calorieTrackerApp.controller;

import com.ai.calorieTrackerApp.models.*;
import com.ai.calorieTrackerApp.records.geminiRecord;
import com.ai.calorieTrackerApp.repositories.userModelRepo;
import com.ai.calorieTrackerApp.service.geminiService;
import com.ai.calorieTrackerApp.service.geminiToFoodCalorie;
import com.ai.calorieTrackerApp.service.userModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PostExchange;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/gemini/ai")
@CrossOrigin(origins = "http://localhost:4200")
public class detailsController {

    @Autowired
    private geminiService service;
    private userModelService userServ;
    private final userModelRepo repo;
    geminiToFoodCalorie gfc = new geminiToFoodCalorie();
    public detailsController(userModelRepo repo){
        this.repo = repo;
    }




    @PostMapping("gemini-pro/generateContent")
    public geminiRecord.GeminiResponse getCompletion(@PathVariable String model, @RequestBody geminiRecord.GeminiRequest request) {
        return service.getCompletion(request);
    }

    @PostMapping("gemini-pro-vision/generateContent")
    public geminiRecord.GeminiResponse getCompletionWithImage(@RequestBody geminiRecord.GeminiRequest request) {
        return service.getCompletionWithImage(request);
    }

    @PostExchange("imgdetail/{userId}/{date}")
    public String imageDetail(@PathVariable long userId, @PathVariable LocalDate date, @ModelAttribute foodImg obj) throws Exception {
        userModel user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        String out = service.getCompletionWithImage(obj.getImg());
        service.saveToFoodCalorie(user,obj.getType(),out,date);

//        service2.saveToFoodCalorie();
        System.out.println(out);
        return out;
    }

    @PostExchange("mealDetail/{userId}/{date}")
    public boolean mealDetail(@PathVariable long userId, @PathVariable LocalDate date, @RequestBody textDto txt)throws Exception {
        userModel user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        String text="In " + txt.getQty()+txt.getUnits()+" of "+txt.getName()+
                ". Determine the calorie in Cal, protein in grams, carbohydrate in grams, fat in grams" +
                "put all details in form of an array as mentioned below " +
                "[item name 1, quantity in item 1, calories in item 1, protein in item 1, carbohydrates in item 1, fat in item 1]" +
                "[item name 2, quantity in item 2, calories in item 2, protein in item 2, carbohydrates in item 2, fat in item 2]" +
                "Each array should be separated by a new line. Do not put the unit in the array.There might be one or more food items in the image. " +
                "Do not put anything other than the mentioned properties.";
        String out=service.getCompletion(text);
        boolean f=service.saveToFoodCalorie(user,txt.getType(),out, date);

//        service2.saveToFoodCalorie();
        System.out.println(f);
        return f;
    }

    @GetMapping("check")
    public void getCheck() throws  Exception{
        gfc.saveToFoodCalorie();
    }

    @GetMapping("dailyfooditem/{userId}/{date}")
    public List<foodCalorie> getFoodCalorieByDateAndUser(@PathVariable long userId, @PathVariable String date){
        userModel user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        LocalDate pDate = LocalDate.parse(date); // Parse date string to LocalDate
        return service.getFoodCalorieByDateAndUser(user, pDate);
    }

    @GetMapping("dailyfooditemtype/{userId}/{date}/{type}")
    public List<foodCalorie> getFoodCalorieByDateUserType(@PathVariable long userId, @PathVariable String date, @PathVariable String type){
        userModel user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        LocalDate pDate = LocalDate.parse(date); // Parse date string to LocalDate
        return service.findByUserAndDateAndItemType(user, pDate, type);
    }
}
