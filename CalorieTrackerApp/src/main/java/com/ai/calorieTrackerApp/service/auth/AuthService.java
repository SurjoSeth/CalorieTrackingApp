package com.ai.calorieTrackerApp.service.auth;

import com.ai.calorieTrackerApp.models.userModel;

public interface AuthService {

    String login(String username, String password);

    String signUp(userModel user);
}
