package com.ramya.repository;

import com.ramya.model.Category;
import com.ramya.model.Food;
import com.ramya.model.SubCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
     List<Food> findByCategory(Category category);

    List<Food> findBySubCategory(SubCategory subCategory);
}
