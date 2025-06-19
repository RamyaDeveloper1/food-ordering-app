
package com.ramya.repository;

import com.ramya.model.Admin;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AdminRepository extends CrudRepository<Admin, String> {

    public Admin findByNameAndPassword(String name, String password);

    public boolean existsByName(String name);

    public Optional<Admin> findByNameIgnoreCase(String name);  

    public void deleteByName(String name);

    public Optional<Admin> findByName(String name);
}

