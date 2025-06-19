
package com.ramya.repository;

import com.ramya.model.Cart;
import com.ramya.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;


public interface CartRepository extends CrudRepository<Cart, Integer> {
  public  List<Cart> findByUserId(Integer userId);

    public Optional<Cart> findByUserIdAndFood_FoodId(Integer userId, Integer foodId);

   public void deleteByUserId(Integer userId);

    public List<Cart> findByUser(User user);

    public void deleteByUser(User user);
}
