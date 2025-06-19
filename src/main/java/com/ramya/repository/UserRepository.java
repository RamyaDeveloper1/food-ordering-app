
package com.ramya.repository;

import com.ramya.model.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{

    public User findByEmailAndPassword(String email, String password);

    public Optional<User> findByEmail(String email);
    
}
