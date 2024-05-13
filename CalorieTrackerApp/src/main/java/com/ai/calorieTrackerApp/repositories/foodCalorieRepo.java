package com.ai.calorieTrackerApp.repositories;

import com.ai.calorieTrackerApp.models.foodCalorie;
import com.ai.calorieTrackerApp.models.userModel;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface foodCalorieRepo extends JpaRepository<foodCalorie, Long> {
    List<foodCalorie> findByUserAndDate(userModel user, LocalDate date);

    List<foodCalorie> findByUserAndDateAndItemType(userModel user, LocalDate date, String itemType);


    //method to find user by emailId and password returns Optional of User
    }

