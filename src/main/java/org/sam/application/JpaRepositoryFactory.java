package org.sam.application;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.util.Assert.notNull;

/**
 * @author: Samir
 * @since 1.0 06/03/2015
 */
@Component
public class JpaRepositoryFactory {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> T getRepository(Class clazz) {
        notNull(clazz);
        notNull(entityManager);
        T crudRepository = (T) new SimpleJpaRepository(clazz, entityManager);
        return crudRepository;
    }
}
