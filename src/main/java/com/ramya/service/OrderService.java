package com.ramya.service;

import com.ramya.model.Order;
import com.ramya.model.User;
import com.ramya.repository.OrderRepository;
import com.ramya.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    UserRepository userRepo;

    @Transactional
    public void placeOrder(Order order) {
        userRepo.save(order.getUser());
        orderRepo.save(order);
        System.out.println("Order placed: " + order.toString());
    }

    public void updateOrderStatus(Integer orderId, String newStatus) {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(newStatus);
            orderRepo.save(order);
        } else {
            throw new IllegalArgumentException("Order not found");
        }
    }

    @Transactional
    public List<Order> getOrdersForUser(Integer userId) {
        List<Order> orders = orderRepo.findOrdersByUserId(userId);

        for (Order order : orders) {
            if (order.getOrderItems() == null) {
                order.setOrderItems(new ArrayList<>());
            }
        }

        return orders;
    }

    public Iterable<Order> getAllOrders() {
        return orderRepo.findAll();
    }

}
