package com.ecommerce.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.ecommerce.entity.*;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure()
                    .addAnnotatedClass(Category.class)
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Order.class)
                    .addAnnotatedClass(OrderDetail.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
