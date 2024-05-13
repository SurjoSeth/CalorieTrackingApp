package com.ai.calorieTrackerApp.service;

import com.ai.calorieTrackerApp.exceptions.DetailsNotFoundException;
import com.ai.calorieTrackerApp.models.userDetails;
import com.ai.calorieTrackerApp.models.userModel;
import com.ai.calorieTrackerApp.repositories.userDetailsRepo;
import com.ai.calorieTrackerApp.repositories.userModelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userDetailsService {

    private final userDetailsRepo repo;
    private final userModelRepo uRepo;

    @Autowired
    private final userModelService usm;
    public userDetailsService(userDetailsRepo repo, userModelRepo uRepo, userModelService usm){
        this.repo = repo;
        this.uRepo = uRepo;
        this.usm = usm;
    }

//    public void createUser() {
//        userModel user = new userModel();
//        user.setUserId(1L);
//        user.setUserName("Rohit");
//        user.setEmailId("rohit123@example.com");
//        user.setPassword("rohit123");
//        user.setContactNumber("9876543210");
//        repo.save(user);
//    }


    double bmrCalculator(userDetails details){
        double bmr=0.0;
        double w= details.getWeight();
        double h= details.getHeight();
        double a= details.getAge();
        if(details.getGender().equals("Male"))
            bmr = (66 + (13.7 * w) + (5 * h) - (6.8*a));
        else
            bmr=(655 + (9.6 *w)+(1.8*h)-(4.7*a));
        return bmr;
    }
    public userDetails createUserDetails(Long userId, userDetails details) {
        if(userId==usm.id)
        {userModel user = uRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        details.setUser(user);
        details.setBMR(bmrCalculator(details));
        return repo.save(details);}
        else
            return null;
    }

    public userDetails updateUserDetails(Long userId, userDetails details) {
        if(userId==usm.id){
        userModel user = uRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userDetails existingDetails = repo.findByUser(user).orElseThrow(() -> new RuntimeException("User not found"));
        existingDetails.setWeight(details.getWeight());
        existingDetails.setHeight(details.getHeight());
        existingDetails.setAge(details.getAge());
        existingDetails.setGender(details.getGender());
        existingDetails.setBMR(bmrCalculator(existingDetails));
//        System.out.println(userId);

        return repo.save(existingDetails);}
        else return null;
    }

    public userDetails getUserDetails(Long userId){
        if(userId==usm.id){
        userModel user = uRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            return repo.findByUser(user)
                    .orElseThrow(()->new DetailsNotFoundException
                            ("Details not found"));
//        return repo.findByUser(user).orElseThrow(() -> new DetailsNotFoundException("Details not found"));
        }
        else return null;
    }


}
