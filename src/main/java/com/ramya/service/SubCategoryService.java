package com.ramya.service;

import com.ramya.model.SubCategory;
import com.ramya.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepository subcatRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addSubCategory(SubCategory subCategory) {
        subcatRepo.save(subCategory);
    }

    public Optional<SubCategory> findSubCategoryById(Integer id) {
        Optional<SubCategory> subCategory = subcatRepo.findById(id);
        System.out.println("SubCategory found: " + subCategory);
        return subCategory;
    }

    @Transactional
    public void deleteSubCategoryByCategoryId(Integer categoryId) {
        subcatRepo.deleteByCategoryId(categoryId);
    }

    public void updateSubCategory(SubCategory subCategory) {
        Optional<SubCategory> existingSubCategory = subcatRepo.findById(subCategory.getId());
        if (existingSubCategory.isPresent()) {
            SubCategory updatedSubCategory = existingSubCategory.get();
            updatedSubCategory.setSubCategoryName(subCategory.getSubCategoryName());
            updatedSubCategory.setCategoryDescription(subCategory.getCategoryDescription());
            updatedSubCategory.setIsAvailable(subCategory.getIsAvailable());
            updatedSubCategory.setCategoryImage(subCategory.getCategoryImage());
            subcatRepo.save(updatedSubCategory);
        }
    }

    public List<SubCategory> findSubCategoryByCategoryId(Integer categoryId) {
        return subcatRepo.findByCategoryId(categoryId);
    }

    public Iterable<SubCategory> getAllSubCategories() {
        return subcatRepo.findAll(); 
    }

    public Boolean isExist(String subCategoryName) {
        return subcatRepo.existsByCategory_CategoryName(subCategoryName);
    }

    @Transactional
    public void deleteSubCategoryById(Integer id) {
        System.out.println("Attempting to delete subcategory with ID: " + id);
        subcatRepo.deleteById(id);
        subcatRepo.flush();
        entityManager.clear(); 
        System.out.println("Subcategory with ID: " + id + " has been deleted.");
    }
}
