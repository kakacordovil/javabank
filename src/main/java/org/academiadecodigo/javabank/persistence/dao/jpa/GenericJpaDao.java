package org.academiadecodigo.javabank.persistence.dao.jpa;

import org.academiadecodigo.javabank.persistence.model.Model;
import org.academiadecodigo.javabank.persistence.dao.Dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * A generic jpa data access object to be used as a base for concrete jpa service implementations
 *
 * @param <T> the model type
 * @see Dao
 */
public abstract class GenericJpaDao<T extends Model> implements Dao<T> {

    protected Class<T> modelType;

    @PersistenceContext
    protected EntityManager em;

    /**
     * Initializes a new JPA DAO instance given a model type
     *
     * @param modelType the model type
     */
    public GenericJpaDao(Class<T> modelType) {
        this.modelType = modelType;
    }

    /**
     * Sets the entity manager
     *
     * @param em the entity manager to set
     */
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * @see Dao#findAll()
     */
    @Override
    public List<T> findAll() {

        CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(modelType);
        Root<T> root = criteriaQuery.from(modelType);
        return em.createQuery(criteriaQuery).getResultList();

        // Using JPA
        //return em.createQuery( "from " + modelType.getSimpleName(), modelType).getResultList();
    }

    /**
     * @see Dao#findById(Integer)
     */
    @Override
    public T findById(Integer id) {
        return em.find(modelType, id);
    }

    /**
     * @see Dao#saveOrUpdate(Model)
     */
    @Override
    public T saveOrUpdate(T modelObject) {
        return em.merge(modelObject);
    }

    /**
     * @see Dao#delete(Integer)
     */
    @Override
    public void delete(Integer id) {
        em.remove(em.find(modelType, id));
    }
}
