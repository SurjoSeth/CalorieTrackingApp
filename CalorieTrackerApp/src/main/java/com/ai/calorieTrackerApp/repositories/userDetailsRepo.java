package com.ai.calorieTrackerApp.repositories;
import com.ai.calorieTrackerApp.models.foodCalorie;
import com.ai.calorieTrackerApp.models.userDetails;
import com.ai.calorieTrackerApp.models.userModel;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userDetailsRepo extends JpaRepository<userDetails, Long> {
    Optional<userDetails> findByUser(userModel user);


}
