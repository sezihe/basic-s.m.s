package com.danielezihe.hibernate.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 03/10/2021
 */
public class HibernateUtilTest {
    private static StandardServiceRegistry mRegistry;
    private static SessionFactory mFactory;

    public static SessionFactory getSessionFactory() {
        // make sure only one instance is returned
        if(mFactory == null) {
            try {
                mRegistry = new StandardServiceRegistryBuilder().configure("hibernate-test.cfg.xml").build();

                MetadataSources metadataSources = new MetadataSources(mRegistry);

                Metadata metadata = metadataSources.getMetadataBuilder().build();

                mFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if(mRegistry != null) {
                    StandardServiceRegistryBuilder.destroy(mRegistry);
                }
            }
        }
        return mFactory;
    }

    public static <T> void addToDB(T obj) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(obj);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void shutDown() {
        if(mRegistry != null) {
            StandardServiceRegistryBuilder.destroy(mRegistry);
        }
    }
}
