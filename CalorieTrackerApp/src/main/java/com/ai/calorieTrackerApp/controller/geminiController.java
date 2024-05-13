package com.ai.calorieTrackerApp.controller;

import com.ai.calorieTrackerApp.records.geminiRecord;
import com.ai.calorieTrackerApp.service.geminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@HttpExchange("/v1beta/models/")
public interface geminiController {


    @GetExchange
    geminiRecord.ModelList getModels();

    @PostExchange("{model}:countTokens")
    geminiRecord.GeminiCountResponse countTokens(
            @PathVariable String model,
            @RequestBody geminiRecord.GeminiRequest request);

    @PostExchange("{model}:generateContent")
    geminiRecord.GeminiResponse getCompletion(
            @PathVariable String model,
            @RequestBody geminiRecord.GeminiRequest request);




}
