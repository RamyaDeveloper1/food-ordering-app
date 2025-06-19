package com.ramya.controller;

import com.ramya.model.Cart;
import com.ramya.model.Food;
import com.ramya.model.Order;
import com.ramya.model.OrderItem;
import com.ramya.model.User;
import com.ramya.service.CartService;
import com.ramya.service.CategoryService;
import com.ramya.service.FoodService;
import com.ramya.service.OrderService;
import com.ramya.service.SubCategoryService;
import com.ramya.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subcatService;

    @Autowired
    private OrderService orderService;

    @GetMapping("viewCart")
    public String viewCart(HttpSession session, Model model) {
        if (session.getAttribute("error") != null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }
        if (session.getAttribute("message") != null) {
            model.addAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        if (session.getAttribute("warning") != null) {
            model.addAttribute("warning", session.getAttribute("warning"));
            session.removeAttribute("warning");
        }
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        List<Cart> carts = cartService.getCartByUser(user.getId());
        model.addAttribute("carts", carts);
        model.addAttribute("user", user);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategories", subcatService.getAllSubCategories());

        Double totalOrderPrice = 0.0;
        for (Cart cart : carts) {
            totalOrderPrice += cart.getFood().getFoodPrice() * cart.getQuantity();
        }

        model.addAttribute("totalOrderPrice", totalOrderPrice);
        return "user/cart";
    }

    public Integer getCartItemCount(User user) {
        List<Cart> userCartItems = cartService.getCartByUser(user.getId());

        int cartItemCount = 0;
        for (Cart cartItem : userCartItems) {
            cartItemCount += cartItem.getQuantity();
        }

        return cartItemCount;
    }
@PostMapping("addCart")
public String addToCart(@RequestParam("foodId") Integer foodId,
                        @RequestParam("foodName") String foodName,
                        @RequestParam("foodPrice") Double foodPrice,
                        @RequestParam("userId") Integer userId,
                        @RequestParam("quantity") Integer quantity, 
                        HttpSession session, Model model) {

    Optional<Food> foodOpt = foodService.findFoodById(foodId);
    if (foodOpt.isEmpty()) {
        return "redirect:/cart/viewCart";
    }
    Food food = foodOpt.get();

    if (quantity > food.getFoodStock()) {
        quantity = food.getFoodStock();
    }

    if (quantity < 1) {
        quantity = 1;
    }

    food.setFoodStock(food.getFoodStock() - quantity);
    foodService.updateFood(food);

    User user = userService.getUserById(userId);

   Cart cart = cartService.findCartByUserAndFood(user, food);
if (cart != null) {
    cart.setQuantity(cart.getQuantity() + quantity); 
    cartService.update(cart);
} else {
    cart = new Cart();
    cart.setFood(food);
    cart.setQuantity(quantity);
    cart.setUser(user);
    cartService.add(cart);
}

    Integer cartItemCount = getCartItemCount(user);
    session.setAttribute("cartItemCount", cartItemCount);

    session.setAttribute("message", "Cart Updated Successfully!");
    return "redirect:/cart/viewCart";
}

  @PostMapping("updateQuantity")
public String updateQuantity(@RequestParam("id") Integer cartId,
                             @RequestParam("quantity") Integer quantity,
                             @RequestParam("foodId") Integer foodId,
                             HttpSession session, Model model) {

    Optional<Cart> cartOpt = cartService.findById(cartId);

    if (cartOpt.isPresent()) {
        Cart cart = cartOpt.get();
        Food food = cart.getFood();

        if (food.getFoodId().equals(foodId)) {
            int currentQuantity = cart.getQuantity();  

            if (quantity == currentQuantity) {
                session.setAttribute("error", "No changes made to the quantity.");
                return "redirect:/cart/viewCart";
            }

            int availableStock = food.getFoodStock();

            if (quantity > availableStock + currentQuantity) {
                session.setAttribute("warning", "Only " + availableStock + " items are available in stock.");
                quantity = availableStock + currentQuantity; 
            }

            if (quantity < 1) {
                quantity = 1;
            }

            int quantityDifference = quantity - currentQuantity;

            if (food.getFoodStock() - quantityDifference < 0) {
                session.setAttribute("warning", "Not enough stock available.");
                return "redirect:/cart/viewCart";
            }

            food.setFoodStock(food.getFoodStock() - quantityDifference);
            foodService.updateFood(food);

            cart.setQuantity(quantity);
            cartService.update(cart);

            session.setAttribute("message", "Quantity updated successfully!");

            model.addAttribute("carts", cartService.getCartItemsForUser(cart.getUser()));
        }
    }

    return "redirect:/cart/viewCart";
}

    @GetMapping("checkout")
    public String checkout(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        List<Cart> carts = cartService.getCartByUser(user.getId());
        if (carts.isEmpty()) {
            model.addAttribute("message", "Your cart is empty. Please add items to proceed.");
            return "user/cart";
        }

        double totalOrderPrice = 0.0;
        for (Cart cart : carts) {
            totalOrderPrice += cart.getFood().getFoodPrice() * cart.getQuantity();
        }
        model.addAttribute("user", user);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategories", subcatService.getAllSubCategories());
        model.addAttribute("carts", carts);
        model.addAttribute("totalPrice", totalOrderPrice);

        return "user/checkout";
    }

    @PostMapping("removeFromCart")
    public String removeFromCart(@RequestParam("cartId") Integer cartId, Model model, HttpSession session) {
        try {
            Cart cart = cartService.findById(cartId).get();
            Food food = cart.getFood();
            Integer quantity = cart.getQuantity();
            cartService.removeItemFromCart(cartId);
            User user = (User) session.getAttribute("user");
            Integer cartItemCount = getCartItemCount(user);
            session.setAttribute("cartItemCount", cartItemCount);
            foodService.updateStock(food.getFoodId(), quantity);

            session.setAttribute("message", "Item removed from the cart and stock updated!");
        } catch (Exception e) {
            session.setAttribute("error", "There was an error removing the item from the cart.");
        }
        return "redirect:/cart/viewCart";
    }

    @PostMapping("placeOrder")
    public String placeOrder(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("mobile") String mobile,
            @RequestParam("address") String address,
            @RequestParam("city") String city,
            @RequestParam("pin") String pin,
            @RequestParam("paymentMethod") String paymentMethod,
            Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "Please log in to place an order.");
            return "redirect:/user/login";
        }

        user = userService.getUserById(user.getId());

        List<Cart> cartItems = cartService.getCartItemsForUser(user);

        if (cartItems == null || cartItems.isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "cart/viewCart";
        }

        Order order = new Order();
        order.setName(name);
        order.setEmail(email);
        order.setMobile(mobile);
        order.setAddress(address);
        order.setCity(city);
        order.setPincode(pin);
        order.setPaymentMethod(paymentMethod);
        order.setOrderDate(LocalDateTime.now());
        double totalPrice = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (Cart cart : cartItems) {
            OrderItem orderItem = new OrderItem(
                    cart.getFood().getFoodName(),
                    cart.getQuantity(),
                    cart.getFood().getFoodPrice()
            );
            orderItems.add(orderItem);
            totalPrice += orderItem.getTotalPrice();
        }

        order.setOrderStatus("pending");
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        order.setUser(user);

        orderService.placeOrder(order);

        cartService.clearCartForUser(user);
        Integer cartItemCount = getCartItemCount(user);
        session.setAttribute("cartItemCount", cartItemCount);

        model.addAttribute("user", user);
        model.addAttribute("carts", cartService.getCartItemsForUser(user));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategories", subcatService.getAllSubCategories());
        model.addAttribute("order", order);
        model.addAttribute("message", "Order Confirmed!.");

        return "user/orderConfirm";
    }

    @GetMapping("myOrders")
    @Transactional
    public String myOrders(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println("User fetched from session: " + user);

        if (user == null) {
            return "redirect:/user/login";
        }
        User getuser = userService.getUserById(user.getId());
        List<Order> orders = getuser.getOrders();
        System.out.println("Orders retrieved: " + orders);

        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderId() + " has " + order.getOrderItems().size() + " items.");
        }

        model.addAttribute("orders", orders);
        return "user/myOrders";
    }

    @PostMapping("updateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderId") Integer orderId,
            @RequestParam("status") String newStatus,
            Model model) {
        orderService.updateOrderStatus(orderId, newStatus);

        model.addAttribute("message", "Order status updated successfully!");

        return "redirect:/cart/myOrders";
    }

}
