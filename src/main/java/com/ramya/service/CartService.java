package com.ramya.service;

import com.ramya.model.Cart;
import com.ramya.model.Food;
import com.ramya.model.User;
import com.ramya.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private FoodService foodService; 
    @Autowired
    private UserService userService;

    public List<Cart> getCartByUser(Integer userId) {
        return cartRepo.findByUserId(userId); 
    }
    public void update(Cart cart) {
        cartRepo.save(cart);
    }

    public Integer getCountCart(Integer userId) {
        List<Cart> carts = cartRepo.findByUserId(userId);
        int count = 0;
        for (Cart cart : carts) {
            count += cart.getQuantity(); 
        }
        return count;
    }

public void addFoodToCart(User user, Food food, Integer quantity) {
    Optional<Cart> existCartItem = cartRepo.findByUserIdAndFood_FoodId(user.getId(), food.getFoodId());

    if (existCartItem.isPresent()) {
        Cart existingCartItem = existCartItem.get();
        existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        cartRepo.save(existingCartItem);
    } else {
        Cart newCartItem = new Cart();
        newCartItem.setUser(user);
        newCartItem.setFood(food);
        newCartItem.setQuantity(quantity);
        cartRepo.save(newCartItem); 
    }
}



    public void removeFoodFromCart(Integer cartId) {
        cartRepo.deleteById(cartId);
    }

    public void updateCartQuantity(Integer cartId, Integer newQuantity) {
        Optional<Cart> cartOpt = cartRepo.findById(cartId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.setQuantity(newQuantity); 
            cartRepo.save(cart); 
        }
    }

    public void clearCart(User user) {
        cartRepo.deleteByUserId(user.getId());
    }

    public Optional<Cart> findById(Integer cartId) {
       return cartRepo.findById(cartId);
    }

    public void add(Cart cart) {
        cartRepo.save(cart);
    }
    public void removeItemFromCart(Integer cartId) {
    cartRepo.deleteById(cartId);
}

 public List<Cart> getCartItemsForUser(User user) {
    return cartRepo.findByUser(user); 
}

 @Transactional
public void clearCartForUser(User user) {
    cartRepo.deleteByUser(user); 
}

public Cart findCartByUserAndFood(User user, Food food) {
    Optional<Cart> cartOpt = cartRepo.findByUserIdAndFood_FoodId(user.getId(), food.getFoodId());
    return cartOpt.orElse(null);
}



  

}
