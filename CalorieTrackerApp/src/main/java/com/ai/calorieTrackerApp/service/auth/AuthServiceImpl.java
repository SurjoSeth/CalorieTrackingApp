package com.ai.calorieTrackerApp.service.auth;

import com.ai.calorieTrackerApp.models.userModel;
import com.ai.calorieTrackerApp.repositories.userModelRepo;
import com.ai.calorieTrackerApp.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final userModelRepo repo;


    @Override
    public String login(String username, String password) {
        var authToken=new UsernamePasswordAuthenticationToken(username,password);
        var authenticate=authenticationManager.authenticate(authToken);
        return JwtUtils.generateToken(((UserDetails)(authenticate.getPrincipal())).getUsername());
    }

    @Override
    public String signUp(userModel user) {
        if(repo.existsByEmailId(user.getEmailId())){
            throw new RuntimeException("User already exists");
        }
        var encodedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        repo.save(user);
        return JwtUtils.generateToken(user.getEmailId());

    }
}
