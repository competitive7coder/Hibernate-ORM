package com.ecommerce;

import com.ecommerce.dao.EcommerceDAO;
import com.ecommerce.entity.*;
import com.ecommerce.util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EcommerceDAO dao = new EcommerceDAO();

        try {
            // 1. Insert Category
            Category electronics = new Category("Electronics", "Electronic Devices and Gadgets");
            dao.insertCategory(electronics);
            System.out.println("Category inserted: " + electronics.getId());

            // 2. Insert Products
            Product laptop = new Product("Laptop", new BigDecimal("1200.50"), 10, electronics);
            Product smartphone = new Product("Smartphone", new BigDecimal("800.00"), 25, electronics);
            dao.insertProduct(laptop);
            dao.insertProduct(smartphone);
            System.out.println("Products inserted.");

            // 3. Insert User
            User user = new User("johndoe", "secret123", "john@example.com", User.Role.CUSTOMER);
            dao.insertUser(user);
            System.out.println("User inserted: " + user.getId() + " | Password hashed: " + user.getPassword());

            // 4. Create Order with multiple OrderDetails
            Order order = new Order(user, LocalDateTime.now());
            
            OrderDetail detail1 = new OrderDetail(order, laptop, 1);
            OrderDetail detail2 = new OrderDetail(order, smartphone, 2);
            
            order.addOrderDetail(detail1);
            order.addOrderDetail(detail2);
            
            dao.createOrder(order);
            System.out.println("Order created with ID: " + order.getId() + " and Total Amount: " + order.getTotalAmount());

            // 5. Fetch Order with details
            Order fetchedOrder = dao.getOrder(order.getId());
            System.out.println("Fetched Order ID: " + fetchedOrder.getId());
            System.out.println("User: " + fetchedOrder.getUser().getUsername());
            for (OrderDetail od : fetchedOrder.getOrderDetails()) {
                System.out.println(" - Product: " + od.getProduct().getName() + " | Qty: " + od.getQuantity() + " | Unit Price: " + od.getUnitPrice());
            }

            // --- Bonus Features Demo ---
            // Named Query
            List<Product> electronicsProducts = dao.getProductsByCategory("Electronics");
            System.out.println("Named Query Found " + electronicsProducts.size() + " electronics.");

            // Criteria Query
            List<Product> allProducts = dao.getAllProductsCriteria();
            System.out.println("Criteria Query Found " + allProducts.size() + " products total.");

            // Pagination
            List<Product> page1 = dao.getProductsPaginated(1, 1);
            System.out.println("Pagination Page 1 (Size 1) contains: " + page1.get(0).getName());

            // Soft Delete
            dao.deleteProductSoft(smartphone.getId());
            System.out.println("Soft deleted smartphone. Remaining active products criteria count: " + dao.getAllProductsCriteria().size());

        } finally {
            HibernateUtil.shutdown();
        }
    }
}
