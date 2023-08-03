package guru.qa.niffler.db.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;

public abstract class JpaTransactionManager {

    protected final EntityManager entityManager;

    public JpaTransactionManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected void persist(Object entity) {
        transaction(entityManager -> entityManager.persist(entity));
    }

    protected void remove(Object entity) {
        transaction(entityManager -> entityManager.remove(entity));
    }

    protected void merge(Object entity) {
        transaction(entityManager -> entityManager.merge(entity));
    }

    private void transaction(Consumer<EntityManager> consumer) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            consumer.accept(entityManager);
            transaction.commit();
        } catch (Exception e) {
         transaction.rollback();
        }
    }
}
