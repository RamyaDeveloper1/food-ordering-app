package com.ramya.controller;

import com.ramya.model.Category;
import com.ramya.model.Food;
import com.ramya.model.SubCategory;
import com.ramya.model.User;
import com.ramya.service.CategoryService;
import com.ramya.service.FoodService;
import com.ramya.service.SubCategoryService;
import com.ramya.service.UserService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SubCategoryService subcatService;

    @Autowired
    FoodService foodService;

    @GetMapping("/")
    public String ShowIndexPage() {
        return "index";
    }

    @GetMapping("about")
    public String about() {
        return "user/about";
    }

    @GetMapping("aboutus")
    public String aboutus() {
        return "about";
    }

    @GetMapping("/register")
    public String register(Model model, HttpSession session) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        return "user/register";
    }

    @PostMapping("addUser")
    public String addUser(@ModelAttribute User user, HttpSession session) {
        User addUser = userService.addUser(user);
        if (addUser != null) {
            session.setAttribute("message", "Registered Successfully!");
            return "redirect:/user/login";

        } else {
            session.setAttribute("error", "Oops! Something went wrong!");
            return "redirect:/user/register";
        }
    }

    @GetMapping("login")
    public String getLoginPage(HttpServletRequest request, Model model, HttpSession session) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String email = null;
            String password = null;

            for (Cookie cookie : cookies) {
                System.out.println("Cookie name: " + cookie.getName() + ", value: " + cookie.getValue());
                if (cookie.getName().equals("email")) {
                    email = cookie.getValue();
                }
                if (cookie.getName().equals("password")) {
                    password = cookie.getValue();
                }
            }

            if (email != null) {
                model.addAttribute("email", email);
            }
            if (password != null) {
                model.addAttribute("password", password);
            }
        }

        return "user/login";
    }

    @PostMapping("login")
    public String addLogin(@RequestParam String email, @RequestParam String password, HttpSession session, HttpServletResponse response) {
        User user = userService.verifyUser(email, password);

        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("message", "Login successful!");
            Cookie emailCookie = new Cookie("email", email);
            Cookie passwordCookie = new Cookie("password", password);

            emailCookie.setMaxAge(60 * 60 * 24 * 7);
            passwordCookie.setMaxAge(60 * 60 * 24 * 7);

            response.addCookie(emailCookie);
            response.addCookie(passwordCookie);

            return "redirect:/user/index";
        } else {
            boolean isRegistered = userService.isRegistered(email);

            if (isRegistered) {
                session.setAttribute("error", "Invalid password. Please try again.");
                return "redirect:/user/login";
            } else {
                session.setAttribute("error", "You are not registered. Please register first.");
                return "redirect:/user/register";
            }
        }
    }

    @GetMapping("index")
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        List<Food> foods = foodService.findAllFoods();
        User user = (User) session.getAttribute("user");
        model.addAttribute("foods", foods);
        model.addAttribute("user", user);
        Integer cartItemCount = (Integer) session.getAttribute("cartItemCount");
        session.setAttribute("cartItemCount", cartItemCount);

        return "user/index";
    }

    @PostMapping("logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        session.setAttribute("message", "Logout successful!");
        session.invalidate();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email") || cookie.getName().equals("password")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        return "redirect:/user/login";
    }

    @GetMapping("profile")
    public String profile(Model model, HttpSession session) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("error", "User is not logged in.");
            return "redirect:/user/login";
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategories", subcatService.getAllSubCategories());

        return "user/profile";
    }

    @PostMapping("updateProfile")
    public String updateProfile(@ModelAttribute User user, HttpSession session) {
        Integer sessionUserId = (Integer) session.getAttribute("userId");
        if (sessionUserId != null && sessionUserId.equals(user.getId())) {
            User existingUser = userService.getUserById(user.getId());
            if (existingUser != null) {
                existingUser.setName(user.getName());
                existingUser.setMobile(user.getMobile());
                existingUser.setAddress(user.getAddress());
                existingUser.setCity(user.getCity());
                existingUser.setState(user.getState());
                existingUser.setPin(user.getPin());

                userService.updateUser(existingUser);

                session.setAttribute("message", "Profile updated successfully!");
                return "redirect:/user/profile";
            } else {
                session.setAttribute("error", "User not found.");
            }
        } else {
            session.setAttribute("error", "You don't have permission to update this profile.");
            return "redirect:/login";
        }

        return "redirect:/user/profile";
    }

    @PostMapping("changePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("error", "You must be logged in to change your password.");
            return "redirect:/user/login";
        }

        User user = userService.getUserById(userId);
        if (user != null) {
            if (!userService.checkPassword(user, currentPassword)) {
                session.setAttribute("error", "Current password is incorrect.");
                return "redirect:/user/profile";
            }

            if (!newPassword.equals(confirmPassword)) {
                session.setAttribute("error", "New passwords do not match.");
                return "redirect:/user/profile";
            }

            user.setPassword(userService.setPassword(newPassword));
            userService.updateUser(user);

            session.setAttribute("message", "Password updated successfully!");
            return "redirect:/user/profile";
        }

        session.setAttribute("error", "User not found.");
        return "redirect:/user/profile";
    }

    @GetMapping("forgotPassword")
    public String forgotPassword(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("Session userId: " + userId);

        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }

        if (userId != null) {
            User user = userService.getUserById(userId);
            session.setAttribute("user", user);
            model.addAttribute("user", user);
            return "user/forgotPassword";
        }

        System.out.println("Redirecting to login. No userId in session.");
        return "redirect:/user/login";
    }

    @PostMapping("resetPassword")
    public String resetPassword(@RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model, HttpSession session) {

        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("error", "Passwords do not match!");
            return "user/forgotPassword";
        }

        Optional<User> user = userService.getUserByEmail(email);

        if (!user.isPresent()) {
            session.setAttribute("error", "No account found with this email address.");
            return "user/forgotPassword";
        }
        User getuser = user.get();
        getuser.setPassword(newPassword);
        userService.updateUser(getuser);
        session.setAttribute("message", "Password has been successfully updated.");

        return "redirect:/user/login";
    }

    @PostMapping("dltAccount")
    public String dltAccount(HttpSession session) {
        System.out.println("Session User: " + session.getAttribute("user"));

        User user = (User) session.getAttribute("user");
        if (user == null) {
            session.setAttribute("error", "You need to be logged in to delete your account.");
            return "redirect:/user/login";
        }

        try {
            Integer userId = user.getId();

            User getUser = userService.getUserById(userId);

            if (getUser != null) {

                userService.deleteUser(getUser);
                session.removeAttribute("user");
                session.setAttribute("message", "Your account has been deleted successfully.");
                session.invalidate();
                return "redirect:/user/login";
            } else {
                session.setAttribute("error", "User not found.");
                return "redirect:/user/profile";
            }
        } catch (Exception e) {

            session.setAttribute("error", "An error occurred while deleting your account. Please try again later.");
            return "redirect:/user/profile";
        }
    }

}
