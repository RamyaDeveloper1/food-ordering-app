
package com.ramya.service;

import com.ramya.model.Admin;
import com.ramya.repository.AdminRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepo;
    
    public List<Admin> getallAdmins(){
        List<Admin> admins=new ArrayList();
        adminRepo.findAll().forEach((admin)->{
            admins.add(admin);
        });
        return admins;
    }
    public Admin addAdmin(Admin admin){
      return adminRepo.save(admin);
    }
      public boolean nameExists(String name) {
        return adminRepo.existsByName(name); 
    }
   public Admin verifyAdmin(String name, String password) {
    Admin admin = adminRepo.findByNameAndPassword(name, password);
    if (admin != null) {
        return admin;
    }
    return null; 
}

    public boolean isRegistered(String name) {
        Optional<Admin> adminDetails = adminRepo.findByName(name);
        return adminDetails.isPresent(); 

    }
    
  
    public Optional<Admin> getAdminByName(String name) {
        String name1 = name.trim().toLowerCase();
        return adminRepo.findByNameIgnoreCase(name1.trim());  
    }

    public void updateAdmin(Admin admin) {
        adminRepo.save(admin);  
    }
   @Transactional
    public void deleteAdminByName(String name) {
        adminRepo.deleteByName(name);  
    }
    
}
  
  


