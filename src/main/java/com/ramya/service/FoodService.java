package com.ramya.service;

import com.ramya.model.Category;
import com.ramya.model.Food;
import com.ramya.model.SubCategory;
import com.ramya.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepo;

    public List<Food> findAllFoods() {
        return foodRepo.findAll();
    }

    public Optional<Food> findFoodById(Integer id) {
        return foodRepo.findById(id);
    }

    public void saveFood(Food newFood) {
         foodRepo.save(newFood);
    }

    public Boolean updateFood( Food food) {
            Food getfood= foodRepo.save(food);
            if(getfood!=null){
              return true;
            }
            return false;
    }


  public boolean deleteFood(Integer id) {
    Optional<Food> food = foodRepo.findById(id);
    if (food.isPresent()) {
        foodRepo.deleteById(id);
        return true; 
    } else {
        return false; 
    }
}

    public Food getFoodById(Integer id) {
        return foodRepo.findById(id).get();
    }

         public List<Food> findFoodsBySubCategory(SubCategory subCategory) {
        return foodRepo.findBySubCategory(subCategory);
    }
      public Optional<List<Food>> getFoodsByCategory(Category category) {
        List<Food> foods = foodRepo.findByCategory(category); 
        return Optional.ofNullable(foods); 
    }
      public void updateStock(int foodId, int quantity) {
       Food food = foodRepo.findById(foodId).get();
    if (food != null) {
        food.setFoodStock(food.getFoodStock() + quantity);
        foodRepo.save(food);
    }
}

}
