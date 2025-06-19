package com.ramya.controller;

import com.ramya.model.Category;
import com.ramya.model.Food;
import com.ramya.model.SubCategory;
import com.ramya.model.User;
import com.ramya.repository.CategoryRepository;
import com.ramya.repository.SubCategoryRepository;
import com.ramya.service.CategoryService;
import com.ramya.service.FoodService;
import com.ramya.service.SubCategoryService;
import java.io.File;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subcatService;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private SubCategoryRepository subcatRepo;

    @GetMapping("index")
    public String index(HttpSession session, Model model) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        List<Food> foods = foodService.findAllFoods();
        model.addAttribute("foods", foods);
        return "user/index";
    }

    @GetMapping("viewFoods")
    public String viewFood(HttpSession session, Model model) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        System.out.println("viewFoods method");
        List<Food> foods = foodService.findAllFoods();
        if (foods == null || foods.isEmpty()) {
            model.addAttribute("errorMessage", "No food items found.");
        }
        model.addAttribute("foods", foods);
        return "admin/viewFood";
    }

    @GetMapping("addFood")
    public String addFood(Model model, HttpSession session) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        Iterable<Category> categories = categoryRepo.findAll();
        List<SubCategory> subcategories = subcatRepo.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        return "admin/addFood";
    }

    @PostMapping("addFood")
    public String addFood(@RequestParam("foodName") String foodName,
            @RequestParam("foodDescription") String foodDescription,
            @RequestParam("foodPrice") Double foodPrice,
            @RequestParam("foodStock") Integer foodStock,
            @RequestParam("foodDiscount") Integer foodDiscount,
            @RequestParam("isAvailable") Boolean isAvailable,
            @RequestParam("category") Category category,
            @RequestParam("subCategory") SubCategory subCategory,
            @RequestParam("foodImage") MultipartFile foodImage,
            HttpSession session, Model model) {
        Double foodDiscountPrice = foodPrice - (foodPrice * (foodDiscount / 100.0));
        System.out.println(foodImage.getOriginalFilename());

        String imageName = "default.jpg";
        if (foodImage != null && !foodImage.isEmpty()) {
            try {
                String uploadDirectory = servletContext.getRealPath("/images/foodImages");
                File uploadDir = new File(uploadDirectory);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                imageName = foodImage.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir.getAbsolutePath(), imageName);
                foodImage.transferTo(imagePath);
                System.out.println("Image transferred successfully");
            } catch (IOException e) {
                session.setAttribute("error", "Failed to upload image: " + e.getMessage());
                return "redirect:/food/addFood";
            }
        }

        Food food = new Food();
        food.setFoodName(foodName);
        food.setFoodDescription(foodDescription);
        food.setFoodPrice(foodPrice);
        food.setFoodStock(foodStock);
        food.setFoodDiscount(foodDiscount);
        food.setFoodDiscountPrice(foodDiscountPrice);
        food.setIsAvailable(isAvailable);
        food.setCategory(category);
        food.setSubCategory(subCategory);
        food.setFoodImage(imageName);

        foodService.saveFood(food);
        session.setAttribute("message", "Food added successfully!");
        return "redirect:/food/viewFoods";
    }

    @GetMapping("editFood/{id}")
    public String editFood(@PathVariable Integer id, Model model) {
        Food food = foodService.getFoodById(id);
        if (food != null) {
            model.addAttribute("food", food);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("subcategories", subcatService.getAllSubCategories());
            return "admin/editFood";
        }
        model.addAttribute("error", "Food Not Found!");
        return "redirect:/food/viewFoods";
    }

    @PostMapping("editFood")
    public String updateFood(
            @RequestParam("foodId") Integer foodId,
            @RequestParam("foodName") String foodName,
            @RequestParam("foodDescription") String foodDescription,
            @RequestParam("foodPrice") Double foodPrice,
            @RequestParam("foodStock") int foodStock,
            @RequestParam("foodDiscount") Integer foodDiscount,
            @RequestParam("isAvailable") boolean isAvailable,
            @RequestParam("category") Integer categoryId,
            @RequestParam("subCategory") Integer subCategoryId,
            @RequestParam(name = "foodImage", required = false) MultipartFile foodImage,
            Model model,
            HttpSession session) {

        try {

            Food food = new Food();
            food.setFoodId(foodId);
            food.setFoodName(foodName);
            food.setFoodDescription(foodDescription);
            food.setFoodPrice(foodPrice);
            food.setFoodStock(foodStock);
            food.setFoodDiscount(foodDiscount);

          if (food.getFoodDiscount() >= 0 && food.getFoodPrice() > 0) {
    double discountPrice = food.getFoodPrice() - (food.getFoodPrice() * (food.getFoodDiscount() / 100.0));
    food.setFoodDiscountPrice(discountPrice);
}

            food.setIsAvailable(isAvailable);
            Category category = categoryService.findCategoryById(categoryId);
            Optional<SubCategory> subCategory = subcatService.findSubCategoryById(subCategoryId);
            food.setCategory(category);
            food.setSubCategory(subCategory.get());

            if (foodImage != null && !foodImage.isEmpty()) {
                String imageName = foodImage.getOriginalFilename();
                File uploadDir = new File("src/main/resources/static/images/foodImages");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                Path imagePath = Paths.get(uploadDir.getAbsolutePath(), imageName);
                foodImage.transferTo(imagePath);
                food.setFoodImage(imageName);
            } else {
                Food existingFood = foodService.getFoodById(foodId);
                food.setFoodImage(existingFood.getFoodImage());
            }

            boolean isUpdated = foodService.updateFood(food);

            if (isUpdated) {
                model.addAttribute("message", "Food updated successfully!");
                return "redirect:/food/viewFoods";
            } else {
                model.addAttribute("error", "Failed to update food. Please try again.");
                return "admin/editFood";
            }

        } catch (IOException e) {
            model.addAttribute("error", "File upload failed: " + e.getMessage());
            return "admin/editFood";
        }
    }

    @GetMapping("deleteFood/{id}")
    public String deleteFood(@PathVariable Integer id, Model model) {
        boolean isDeleted = foodService.deleteFood(id);
        if (isDeleted) {
            model.addAttribute("message", "Food item deleted successfully.");
        } else {
            model.addAttribute("error", "Food item with ID " + id + " not found.");
        }
        return "redirect:/food/viewFoods";
    }
@GetMapping("details/{foodId}")
public String viewFoodDetails(@PathVariable("foodId") Integer foodId, Model model, HttpSession session) {
    Food food = foodService.getFoodById(foodId);
    model.addAttribute("food", food);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);  
          model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("subcategories", subcatService.getAllSubCategories());
    return "user/foodDetails"; 
}
@GetMapping("individual/{foodId}")
public String individualFood(@PathVariable Integer foodId, Model model){
Food selectedFood = foodService.getFoodById(foodId);
        List<Food> allFoods = foodService.findAllFoods();
        model.addAttribute("food", selectedFood);
        model.addAttribute("foods", allFoods);
           model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("subcategories", subcatService.getAllSubCategories());
        return "user/food"; 
}
}
