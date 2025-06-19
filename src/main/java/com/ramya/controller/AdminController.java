package com.ramya.controller;

import com.ramya.model.Admin;
import com.ramya.model.Order;
import com.ramya.model.User;
import com.ramya.service.AdminService;
import com.ramya.service.OrderService;
import com.ramya.service.UserService;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin("*")
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    



    @GetMapping("/adminregister")
    public String getAdminPage(Model model, HttpSession session) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }

        return "admin/register";
    }

    @PostMapping("addAdmin")
    public String addAdmin(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword, HttpSession session) {

        if (adminService.nameExists(name)) {
            session.setAttribute("error", "Username taken. Try another.");
            return "redirect:/admin/adminregister";
        } else {
            Admin admin = new Admin();
            admin.setName(name);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.setConfirmPassword(confirmPassword);
            adminService.addAdmin(admin);

            session.setAttribute("message", "Admin registered successfully!");
            return "redirect:/admin/login";
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
            String name = null;
            String password = null;

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("name")) {
                    name = cookie.getValue();
                }
                if (cookie.getName().equals("password")) {
                    password = cookie.getValue();
                }
            }

            if (name != null) {
                model.addAttribute("name", name);
            }
            if (password != null) {
                model.addAttribute("password", password);
            }
        }

        return "admin/login";
    }

    @PostMapping("login")
    public String addLogin(@RequestParam String name, @RequestParam String password, HttpSession session, Model model, HttpServletResponse response) {
        Admin admin = adminService.verifyAdmin(name, password);

        if (admin != null) {
            session.setAttribute("adminDetails", admin);
            session.setAttribute("message", "Login successful!");
            Cookie nameCookie = new Cookie("name", name);
            Cookie passwordCookie = new Cookie("password", password);

            nameCookie.setMaxAge(60 * 60 * 24 * 7);
            passwordCookie.setMaxAge(60 * 60 * 24 * 7);

            nameCookie.setPath("/");
            passwordCookie.setPath("/");

            response.addCookie(nameCookie);
            response.addCookie(passwordCookie);

            return "redirect:/admin/index";
        } else {
            boolean isRegistered = adminService.isRegistered(name);

            if (isRegistered) {
                session.setAttribute("error", "Invalid password. Please try again.");
                return "redirect:/admin/login";
            } else {
                session.setAttribute("error", "You are not registered. Please register first.");
                return "redirect:/admin/adminregister";
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

        return "admin/index";
    }

    @GetMapping("about")
    public String about() {
        return "about";
    }

    @PostMapping("adminlogout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.setAttribute("message", "Logout successful!");

        session.invalidate();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("name") || cookie.getName().equals("password")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        return "redirect:/admin/login";
    }

    @GetMapping("viewAdmins")
    public String viewAdmins(Model model, HttpSession session) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }

        List<Admin> admins = adminService.getallAdmins();
        model.addAttribute("admins", admins);
        return "admin/viewAdmins";
    }

    @GetMapping("edit/{name}")
    public String editAdmin(@PathVariable("name") String name, Model model) {
        String trimmedName = name.trim();
        System.out.println("EDIT: " + trimmedName);

        Optional<Admin> admin = adminService.getAdminByName(trimmedName);
        if (admin.isPresent()) {
            model.addAttribute("admin", admin.get());
            return "admin/editAdmin";
        }

        return "redirect:/admin/viewAdmins";
    }

    @PostMapping("edit")
    public String updateAdmin(@ModelAttribute Admin admin, HttpSession session) {
        String trimmedName = admin.getName() != null ? admin.getName().trim() : null;
        if (trimmedName == null || trimmedName.isEmpty()) {
            session.setAttribute("error", "No update was applied.");
            return "redirect:/admin/viewAdmins";
        }

        Optional<Admin> existingAdmin = adminService.getAdminByName(trimmedName);

        if (existingAdmin.isPresent()) {
            Admin updateAdmin = existingAdmin.get();

            if (!trimmedName.equals(updateAdmin.getName())) {
                updateAdmin.setName(trimmedName);
            }

            if (!admin.getEmail().equals(updateAdmin.getEmail())) {
                updateAdmin.setEmail(admin.getEmail());
            }

            adminService.updateAdmin(updateAdmin);

            session.setAttribute("message", "Admin details updated successfully!");
            return "redirect:/admin/viewAdmins";
        }

        session.setAttribute("message", "Error: Admin not found.");
        return "redirect:/admin/viewAdmins";
    }

    @GetMapping("delete/{name}")
    public String deleteAdmin(@PathVariable("name") String name, HttpSession session) {
        String trimmedName = name.trim();
        System.out.println("Attempting to delete admin with name: " + trimmedName);
        adminService.deleteAdminByName(trimmedName);
        if (adminService.getallAdmins().isEmpty()) {
            return "redirect:/admin/adminregister";
        }
        session.setAttribute("message", "Deleted Successfully!");

        return "redirect:/admin/viewAdmins";
    }

    @GetMapping("users")
    public String getAllUsers(Model model, HttpSession session) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }
@GetMapping("orders")
public String orders(Model model) {
    Iterable<Order> allOrders = orderService.getAllOrders();
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    List<String> formattedOrderDates = new ArrayList<>();
    for (Order order : allOrders) {
        if (order.getOrderDate() != null) {
            String formattedDate = order.getOrderDate().format(formatter);
            formattedOrderDates.add(formattedDate);
        }
    }

    model.addAttribute("orders", allOrders);
    model.addAttribute("formattedOrderDates", formattedOrderDates);

    return "admin/orders";
}



        }
