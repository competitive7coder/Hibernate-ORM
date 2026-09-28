package com.ecommerce.dao;

import com.ecommerce.entity.*;
import com.ecommerce.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;

public class EcommerceDAO {

    public void insertCategory(Category category) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(category);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void insertProduct(Product product) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void insertUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void createOrder(Order order) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Order getOrder(Long orderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Fetch Order with Users and OrderDetails (Products)
            Order order = session.createQuery(
                "SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.orderDetails od JOIN FETCH od.product WHERE o.id = :id", Order.class)
                .setParameter("id", orderId)
                .uniqueResult();
            return order;
        }
    }

    // Named Query usage
    public List<Product> getProductsByCategory(String categoryName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createNamedQuery("Product.findByCategoryName", Product.class);
            query.setParameter("categoryName", categoryName);
            return query.getResultList();
        }
    }

    // Criteria Query usage
    public List<Product> getAllProductsCriteria() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cr = cb.createQuery(Product.class);
            Root<Product> root = cr.from(Product.class);
            cr.select(root);
            
            return session.createQuery(cr).getResultList();
        }
    }
    
    // Pagination usage
    public List<Product> getProductsPaginated(int pageNumber, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery("FROM Product", Product.class);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
    }
    
    // Soft delete product
    public void deleteProductSoft(Long productId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, productId);
            if (product != null) {
                session.remove(product); // Trigger SQLDelete
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
