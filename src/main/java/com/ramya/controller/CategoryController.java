package com.ramya.controller;

import com.ramya.model.Category;
import com.ramya.model.Food;
import com.ramya.model.SubCategory;
import com.ramya.service.CategoryService;
import com.ramya.service.FoodService;
import com.ramya.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin(value = "*")
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subcatService;

    @Autowired
    private FoodService foodService;

    @GetMapping("viewCategory")
    public String viewCategory(Model model, HttpSession session) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        Iterable<SubCategory> subCategories = subcatService.getAllSubCategories();
        System.out.println(subCategories);
        model.addAttribute("subCategories", subCategories);
        return "admin/viewCategory";
    }

    @GetMapping("addCategory")
    public String addCategory(Model model, HttpSession session) {
        List<Category> categories = categoryService.getAllCategories();
        Iterable<SubCategory> subCategories = subcatService.getAllSubCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("subCategories", subCategories);
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }

        return "admin/addCategory";
    }

    @PostMapping("addCategory")
    public String addCategory(@RequestParam("categoryName") String categoryName,
            @RequestParam("subCategoryName") String subCategoryName,
            @RequestParam("categoryDescription") String categoryDescription,
            @RequestParam(value = "isAvailable", defaultValue = "false") boolean isAvailable,
            @RequestParam("categoryImage") MultipartFile categoryImage,
            Model model, HttpSession session) {

        if (subcatService.isExist(subCategoryName)) {
            session.setAttribute("error", "SubCategory Name already exists");
            return "redirect:/category/addCategory";
        }

        List<Category> categories = categoryService.findByName(categoryName);

        if (categories.isEmpty()) {
            Category category = new Category();
            category.setCategoryName(categoryName);
            categoryService.addCategory(category);
            categories.add(category);
        }
        Category category = categories.get(0);

        String imageName = "default.jpg";
        if (categoryImage != null && !categoryImage.isEmpty()) {
            imageName = categoryImage.getOriginalFilename();
            try {
                File uploadDir = new File("src/main/resources/static/images/categoryImages");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                Path imagePath = Paths.get(uploadDir.getAbsolutePath(), imageName);
                categoryImage.transferTo(imagePath);
            } catch (Exception e) {
                session.setAttribute("error", "Failed to upload image: " + e.getMessage());
                return "redirect:/category/addCategory";
            }
        }

        SubCategory subCategory = new SubCategory();
        subCategory.setSubCategoryName(subCategoryName);
        subCategory.setCategory(category);
        subCategory.setCategoryDescription(categoryDescription);
        subCategory.setIsAvailable(isAvailable);
        subCategory.setCategoryImage(imageName);

        subcatService.addSubCategory(subCategory);
        category.getSubCategories().add(subCategory);

        categoryService.updateCategory(category);

        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory);
        session.setAttribute("message", "Category and SubCategory added successfully");

        return "redirect:/category/viewCategory";
    }

    @GetMapping("editCategory/{id}")
    public String editCategory(@PathVariable Integer id, Model model, HttpSession session) {

        Optional<SubCategory> subCategoryOptional = subcatService.findSubCategoryById(id);

        if (!subCategoryOptional.isPresent()) {
            session.setAttribute("error", "SubCategory Not Found!");
            return "admin/viewCategory";
        }

        SubCategory subCategory = subCategoryOptional.get();
        Category category = subCategory.getCategory();
        List<SubCategory> subcategories = subcatService.findSubCategoryByCategoryId(category.getId());

        model.addAttribute("subCategory", subCategory);
        model.addAttribute("category", category);
        model.addAttribute("subCategories", subcategories);

        return "admin/editCategory";
    }

    @PostMapping("updateCategory")
    @Transactional
    public String updateCategory(@RequestParam("id") Integer subCategoryId,
            @RequestParam(value = "categoryImage", required = false) MultipartFile categoryImage,
            @RequestParam(value = "subCategoryName", required = false) String subCategoryName,
            @RequestParam(value = "categoryDescription", required = false) String categoryDescription,
            @RequestParam(value = "isAvailable", defaultValue = "false") boolean isAvailable,
            Model model, HttpSession session) {

        try {

            SubCategory subCategory = subcatService.findSubCategoryById(subCategoryId).orElse(null);

            if (subCategory == null) {
                session.setAttribute("error", "SubCategory not found");
                return "redirect:/category/viewCategory";
            }

            if (subCategoryName != null) {
                subCategory.setSubCategoryName(subCategoryName);
            }
            if (categoryDescription != null) {
                subCategory.setCategoryDescription(categoryDescription);
            }
            subCategory.setIsAvailable(isAvailable);

            if (categoryImage != null && !categoryImage.isEmpty()) {
                String imageName = categoryImage.getOriginalFilename();
                File uploadDir = new File("src/main/resources/static/images/categoryImages");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                Path imagePath = Paths.get(uploadDir.getAbsolutePath(), imageName);
                categoryImage.transferTo(imagePath);
                subCategory.setCategoryImage(imageName);
            }

            subcatService.updateSubCategory(subCategory);

            List<SubCategory> updatedSubCategories = subcatService.findSubCategoryByCategoryId(subCategory.getCategory().getId());
            model.addAttribute("subCategories", updatedSubCategories);

            session.setAttribute("message", "SubCategory updated successfully");

        } catch (Exception e) {
            session.setAttribute("error", "Failed to update subcategory: " + e.getMessage());
        }

        return "redirect:/category/viewCategory";
    }

    @GetMapping("deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Integer subCategoryId, Model model, HttpSession session) {
        System.out.println("Delete request received for subcategory ID: " + subCategoryId);
        SubCategory subCategory = subcatService.findSubCategoryById(subCategoryId).orElse(null);

        if (subCategory != null) {
            System.out.println("Deleting subcategory: " + subCategory.getSubCategoryName());

            try {

                subcatService.deleteSubCategoryById(subCategoryId);
                System.out.println("Subcategory deleted successfully: " + subCategory.getSubCategoryName());
                session.setAttribute("message", "SubCategory deleted successfully.");
            } catch (Exception e) {
                System.out.println("Error occurred during deletion: " + e.getMessage());
                session.setAttribute("error", "Failed to delete SubCategory.");
            }

        } else {
            System.out.println("SubCategory with ID " + subCategoryId + " not found.");
            session.setAttribute("error", "SubCategory Not Found.");
        }
        Iterable<SubCategory> subCategories = subcatService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);
        return "redirect:/category/viewCategory";
    }

@GetMapping("{categoryId}")
public String getCategoryFoods(@PathVariable Integer categoryId, Model model) {
    Category category = categoryService.findCategoryById(categoryId);
    if (category != null) {
        List<Food> foods = foodService.getFoodsByCategory(category)
                                       .orElse(new ArrayList<>()); 
        model.addAttribute("category", category);
        model.addAttribute("foods", foods);
       List<Category> allCategories = categoryService.getAllCategories();
           model.addAttribute("categories", allCategories);

    } else {
        model.addAttribute("error", "Category not found!");
    }
    return "user/category";
}
    @GetMapping("{categoryId}/subCategory/{subCategoryId}")
public String getSubCategoryFoods(@PathVariable Integer categoryId, @PathVariable Integer subCategoryId, Model model) {
    Optional<Category> categoryOpt = Optional.ofNullable(categoryService.findCategoryById(categoryId));
    Optional<SubCategory> subCategoryOpt = subcatService.findSubCategoryById(subCategoryId);
      List<Category> allCategories = categoryService.getAllCategories();
      
        System.out.println("subcategory method came");
    if (categoryOpt.isPresent() && subCategoryOpt.isPresent()) {
        Category category = categoryOpt.get();
        SubCategory subCategory = subCategoryOpt.get();
        
        List<Food> foods = foodService.findFoodsBySubCategory(subCategory);
        model.addAttribute("category", category);
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("foods", foods);
         model.addAttribute("categories", allCategories);
    } else {
        model.addAttribute("error", "Category or SubCategory not found");
        return "user/index"; 
    }

    return "user/subcategory"; 
}

   
}
