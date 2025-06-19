
package com.ramya.service;

import com.ramya.model.Category;
import com.ramya.repository.CategoryRepository;
import com.ramya.repository.SubCategoryRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepo;
    
    @Autowired
    SubCategoryRepository subcatRepo;

    public void addCategory(Category category) {
           categoryRepo.save(category);
    }

     public List<Category> getAllCategories() {
         List<Category> allCategory=new ArrayList();
         categoryRepo.findAll().forEach((e)->{
              allCategory.add(e);
        });
         return allCategory;
    }

   

    public Category findCategoryById(Integer id) {
        return categoryRepo.findById(id).orElse(null);
    }
    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepo.deleteById(id);
    }

   public void updateCategory(Category category) {
    categoryRepo.save(category); 
}

   public List<Category> findByName(String categoryName) {
    return categoryRepo.findByCategoryName(categoryName);
}

  

}
