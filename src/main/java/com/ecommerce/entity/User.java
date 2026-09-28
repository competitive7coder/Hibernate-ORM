package com.ecommerce.entity;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public enum Role {
        ADMIN, CUSTOMER
    }

    public User() {}

    public User(String username, String rawPassword, String email, Role role) {
        this.username = username;
        setPassword(rawPassword);
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    // Hash password before saving
    public void setPassword(String rawPassword) {
        this.password = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }
    
    public boolean checkPassword(String rawPassword) {
        return BCrypt.checkpw(rawPassword, this.password);
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    
    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }
}
