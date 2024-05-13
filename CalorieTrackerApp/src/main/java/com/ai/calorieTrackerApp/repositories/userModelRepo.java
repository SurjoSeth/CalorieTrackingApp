package com.ai.calorieTrackerApp.repositories;

import com.ai.calorieTrackerApp.models.userModel;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userModelRepo extends JpaRepository<userModel, Long> {

    boolean existsByEmailId(String emailId);

    Optional<userModel> findByEmailId(String username);
}