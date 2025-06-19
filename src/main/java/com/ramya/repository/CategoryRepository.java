
package com.ramya.repository;

import com.ramya.model.Category;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer>{

    public List<Category> findByCategoryName(String categoryName);
    
     List<Category> findAllByIdIn(List<Integer> ids);

    
}



