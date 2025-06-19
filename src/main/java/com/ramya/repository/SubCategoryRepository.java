package com.ramya.repository;

import com.ramya.model.Category;
import com.ramya.model.SubCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {

    void deleteByCategoryId(Integer categoryId);
   

   List<SubCategory> findByCategoryId(Integer categoryId);

  Boolean existsByCategory_CategoryName(String subCategoryName);

          List<SubCategory> findAllByIdIn(List<Integer> ids);

}
