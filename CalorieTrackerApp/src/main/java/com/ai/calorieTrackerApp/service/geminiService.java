package com.ai.calorieTrackerApp.service;

import com.ai.calorieTrackerApp.controller.geminiController;
import com.ai.calorieTrackerApp.models.foodCalorie;
import com.ai.calorieTrackerApp.models.userModel;
import com.ai.calorieTrackerApp.records.geminiRecord;
import com.ai.calorieTrackerApp.repositories.foodCalorieRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class geminiService {
    public static final String GEMINI_PRO = "gemini-pro";
    public static final String GEMINI_1_5_PRO = "gemini-1.5-pro-latest";
    public static final String GEMINI_PRO_VISION = "gemini-pro-vision";

    private final geminiController geminiInterface;


    @Autowired
    private final foodCalorieRepo foodcalorierepo;
    private final userModelService ums;

    public geminiService(geminiController geminiInterface, foodCalorieRepo foodcalorierepo, userModelService ums) {
        this.geminiInterface = geminiInterface;
        this.foodcalorierepo = foodcalorierepo;
        this.ums = ums;
    }

    public geminiRecord.ModelList getModels() {
        return geminiInterface.getModels();
    }

    public static void someStaticMethod(geminiService service) {
        // Now you can access geminiInterface through the service parameter
        geminiRecord.ModelList models = service.getModels();
    }

    public geminiRecord.GeminiCountResponse countTokens(String model, geminiRecord.GeminiRequest request) {
        return geminiInterface.countTokens(model, request);
    }

    public int countTokens(String text) {
        geminiRecord.GeminiCountResponse response = countTokens(GEMINI_PRO, new geminiRecord.GeminiRequest(
                List.of(new geminiRecord.Content(List.of(new geminiRecord.TextPart(text))))));
        return response.totalTokens();
    }

    public geminiRecord.GeminiResponse getCompletion(geminiRecord.GeminiRequest request) {
        return geminiInterface.getCompletion(GEMINI_PRO, request);
    }

    public geminiRecord.GeminiResponse getCompletionWithModel(String model, geminiRecord.GeminiRequest request) {
        return geminiInterface.getCompletion(model, request);
    }


    public geminiRecord.GeminiResponse getCompletionWithImage(geminiRecord.GeminiRequest request) {
        return geminiInterface.getCompletion(GEMINI_PRO_VISION, request);
    }

    public geminiRecord.GeminiResponse analyzeImage(geminiRecord.GeminiRequest request) {
        return geminiInterface.getCompletion(GEMINI_1_5_PRO, request);
    }

    public String getCompletion(String text) {
        geminiRecord.GeminiResponse response = getCompletion(new geminiRecord.GeminiRequest(
                List.of(new geminiRecord.Content(List.of(new geminiRecord.TextPart(text))))));
        return response.candidates().get(0).content().parts().get(0).text();
    }

//    public String getCompletionWithImage(String text, String imageFileName) throws IOException {
//        geminiRecord.GeminiResponse response = getCompletionWithImage(
//                new geminiRecord.GeminiRequest(List.of(new geminiRecord.Content(List.of(
//                        new geminiRecord.TextPart(text),
//                        new geminiRecord.InlineDataPart(new geminiRecord.InlineData("image/png",
//                                Base64.getEncoder().encodeToString(Files.readAllBytes(
//                                        Path.of("src/main/resources/", imageFileName))))))
//                ))));
//        System.out.println(response);
//        return response.candidates().get(0).content().parts().get(0).text();
//    }

    public String getCompletionWithImage(MultipartFile imgFile) throws IOException {
        byte[] img=imgFile.getBytes();
        String b64Img=Base64.getEncoder().encodeToString(img);
        geminiRecord.GeminiResponse response = getCompletionWithImage(
                new geminiRecord.GeminiRequest(List.of(new geminiRecord.Content(List.of(
                        new geminiRecord.TextPart("Analyze this image of food as accurately as possible." +
                                "Determine the item name, quantity in grams, calorie in Cal, protein in grams, carbohydrate in grams, fat in grams" +
                                "put all details in form of an array as mentioned below " +
                                "[item name 1, quantity in item 1, calories in item 1, protein in item 1, carbohydrates in item 1, fat in item 1]" +
                                "[item name 2, quantity in item 2, calories in item 2, protein in item 2, carbohydrates in item 2, fat in item 2]" +
                                "Each array should be separated by a new line. Do not put the unit in the array.There might be one or more food items in the image. Do not put anything other than the mentioned properties."),
                        new geminiRecord.InlineDataPart(new geminiRecord.InlineData("image/png",
                                b64Img)))
                ))));
//        System.out.println(response);
        return response.candidates().get(0).content().parts().get(0).text();
    }

    public String analyzeImage(String text, String imageFileName) throws IOException {
        geminiRecord.GeminiResponse response = analyzeImage(
                new geminiRecord.GeminiRequest(List.of(new geminiRecord.Content(List.of(
                        new geminiRecord.TextPart(text),
                        new geminiRecord.InlineDataPart(new geminiRecord.InlineData("image/png",
                                Base64.getEncoder().encodeToString(Files.readAllBytes(
                                        Path.of("src/main/resources/", imageFileName))))))
                ))));
        System.out.println(response);
        return response.candidates().get(0).content().parts().get(0).text();
    }

    public boolean saveToFoodCalorie(userModel user, String itemType, String out, LocalDate date) {
            System.out.println(ums.id);
            System.out.println(user.getUserId());
        if(user.getUserId()==ums.id){
        String[] food = out.trim().split("\n");
        for (String f : food) {
            f = f.substring(1, f.length() - 1);
            foodCalorie foodcalorie = new foodCalorie();
            foodcalorie.setDate(date);
            foodcalorie.setTime(LocalTime.now());
            foodcalorie.setUser(user);
            foodcalorie.setItemType(itemType);
            String[] property = f.split(",");
            foodcalorie.setItemName(property[0]);
            foodcalorie.setQuantity(Double.parseDouble(property[1]));
            foodcalorie.setCalories(Double.parseDouble(property[2]));
            foodcalorie.setProtein(Double.parseDouble(property[3]));
            foodcalorie.setCarbohydrates(Double.parseDouble(property[4]));
            foodcalorie.setFat(Double.parseDouble(property[5]));
            foodcalorierepo.save(foodcalorie);
        }
        return true;
        }
        else return false;
    }

    public List<foodCalorie> getFoodCalorieByDateAndUser(userModel user, LocalDate date) {
        // Leverage LocalDate for date-only filtering
        if(user.getUserId()==ums.id)
        return foodcalorierepo.findByUserAndDate(user, date);
        else
            return null;
    }


    public List<foodCalorie> findByUserAndDateAndItemType(userModel user, LocalDate pDate, String type) {
        if(user.getUserId()==ums.id)
        return foodcalorierepo.findByUserAndDateAndItemType(user, pDate, type);
        else
            return null;
    }
}


