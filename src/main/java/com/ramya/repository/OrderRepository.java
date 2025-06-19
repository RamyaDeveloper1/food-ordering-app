
package com.ramya.repository;

import com.ramya.model.Order;
import com.ramya.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {


    public List<Order> findByUser(User user);
    
   public List<Order> findByUserId(Integer userId);

    public List<Order> findOrdersByUserId(Integer userId);
    
}
