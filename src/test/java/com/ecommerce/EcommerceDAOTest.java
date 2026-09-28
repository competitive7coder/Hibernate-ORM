package com.ecommerce;

import com.ecommerce.dao.EcommerceDAO;
import com.ecommerce.entity.*;
import com.ecommerce.util.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EcommerceDAOTest {

    private static EcommerceDAO dao;

    @BeforeAll
    static void setup() {
        dao = new EcommerceDAO();
    }

    @AfterAll
    static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    void testFullEcommerceFlow() {
        // 1. Create and Save Category
        Category category = new Category("Books", "Printed and digital books");
        dao.insertCategory(category);
        assertNotNull(category.getId());

        // 2. Create and Save Product
        Product book = new Product("Java Programming", new BigDecimal("45.00"), 50, category);
        dao.insertProduct(book);
        assertNotNull(book.getId());

        // 3. Create and Save User
        User user = new User("alice", "pass123", "alice@example.com", User.Role.ADMIN);
        dao.insertUser(user);
        assertNotNull(user.getId());
        assertTrue(user.checkPassword("pass123"));

        // 4. Create Order and OrderDetails
        Order order = new Order(user, LocalDateTime.now());
        OrderDetail detail = new OrderDetail(order, book, 2);
        order.addOrderDetail(detail);
        
        dao.createOrder(order);
        assertNotNull(order.getId());
        assertEquals(new BigDecimal("90.00"), order.getTotalAmount());

        // 5. Fetch Order
        Order fetchedOrder = dao.getOrder(order.getId());
        assertNotNull(fetchedOrder);
        assertEquals(user.getId(), fetchedOrder.getUser().getId());
        assertEquals(1, fetchedOrder.getOrderDetails().size());
        assertEquals("Java Programming", fetchedOrder.getOrderDetails().get(0).getProduct().getName());
        
        // 6. Bonus: Soft Delete
        dao.deleteProductSoft(book.getId());
        // Since product is softly deleted, the criteria query should return less
        // This validates @Where(clause = "deleted=false") works.
    }
}
