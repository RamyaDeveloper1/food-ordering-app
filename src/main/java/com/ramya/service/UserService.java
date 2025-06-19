package com.ramya.service;

import com.ramya.model.Order;
import com.ramya.model.User;
import com.ramya.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public List<User> getAllUsers() {
        return (List<User>) userRepo.findAll();
    }

    public User addUser(User user) {
        User addUser = userRepo.save(user);
        return addUser;
    }
public User verifyUser(String email, String password) {
    User user = userRepo.findByEmailAndPassword(email, password);
    return user;
}

  public boolean isRegistered(String email) {
    try {
        Optional<User> userDetails = userRepo.findByEmail(email);
        return userDetails.isPresent();
    } catch (Exception e) {
        System.out.println("Error checking registration for email: " + email);
        return false;  
    }
}
   
    public User updateUser(User user) {
        return userRepo.save(user);
    }
  
    public User getUserById(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    public boolean checkPassword(User user, String currentPassword) {
        Optional<User> getuser=userRepo.findById(user.getId());
         if(getuser.isPresent()){
              String existPassword=getuser.get().getPassword();
              if(currentPassword.equals(existPassword)){
              return true;
              }
         }
        return false;
    }

    public String setPassword(String newPassword) {
       return newPassword;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public void deleteUser(User user) {
        
        userRepo.deleteById(user.getId());
    
    }
 



}
