/*
    Copyright 2011 Silvio Wehner, Franziska Staake, Thomas Schulze
   
    This file is part of HIS.

    HIS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HIS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HIS.  If not, see <http://www.gnu.org/licenses/>.
 */
package his.model.providers;

import his.model.interfaces.ICrud;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.config.PersistenceUnitProperties;

/**
 *
 * @author silvio
 */
public class BaseProvider<T> implements ICrud<T> {
        
    @PersistenceContext protected EntityManager entityManager;
        
    protected BaseProvider() {
        Map properties = new HashMap();
        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, "jnafoxiz");
        
        this.entityManager = Persistence.createEntityManagerFactory("HISPU", properties)
            .createEntityManager();
    }
    
    @Override
    public T findByID(Serializable id) {
        try {
            Class<T> clazz = getClassType();
            String className = clazz.getSimpleName();
            TypedQuery<T> tQuery = entityManager.createNamedQuery( className + ".findById", clazz);
            T result = tQuery.setParameter("id", id).getSingleResult();
            return result;
        }
        catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public void update(T t) {
        create(t);
    }

    @Override
    public void delete(T t) {
        entityManager.getTransaction().begin();
        entityManager.remove(t);
        entityManager.getTransaction().commit();
    }

    @Override
    public void create(T t) {
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.getTransaction().commit();
    }

    @Override
    public Collection<T> findAll() {
        try {            
            Class<T> clazz = getClassType();
            String className = clazz.getSimpleName();
            TypedQuery<T> tQuery = entityManager.createNamedQuery( className + ".findById", clazz);
            Collection<T> result = tQuery.getResultList();
            return result;
        }
        catch(NoResultException e) {
            return null;
        }
    }

    private Class getClassType() {        
        ParameterizedType parametrizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class clazz = (Class) parametrizedType.getActualTypeArguments()[0];
        
        return clazz;
    }
}
