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

import his.exceptions.modelexceptions.QueryNotPossibleException;
import his.model.interfaces.ICrud;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
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
        Map properties = setDatabaseProperties();
        
        
        this.entityManager = Persistence.createEntityManagerFactory("HISPU", properties)
            .createEntityManager();
    }
    
    @Override
    public T findById(Serializable id) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        try {
            return findSingleResultByQueryName("findById", parameters);
        } catch (QueryNotPossibleException ex) {
            // Diese Exception sollte nie auftreten!
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(T t) {
        entityManager.getTransaction().begin();
        entityManager.flush();
        entityManager.getTransaction().commit();
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
        return findCollectionByQueryName("findById");
    }
    
    protected Collection<T> findCollectionByQueryName(String queryName) {
        return findCollectionByQueryName(queryName, null);
    }
    
    protected Collection<T> findCollectionByQueryName(String queryName, Map<String, Object> parameters) {
        try {
            Class<T> clazz = getClassType();
            String className = clazz.getSimpleName();
            TypedQuery<T> tQuery = entityManager.createNamedQuery( className + "." + queryName, clazz);
            if (parameters != null) {
                for( Entry<String, Object> entry : parameters.entrySet() ) {
                    tQuery.setParameter(entry.getKey(), entry.getValue());
                }
            }
            Collection<T> result = tQuery.getResultList();
            return result;
        }
        catch(NoResultException e) {
            return null;
        }
    }
    
    protected T findSingleResultByQueryName(String queryName, Map<String, Object> parameters) throws QueryNotPossibleException {
        try {
            Class<T> clazz = getClassType();
            String className = clazz.getSimpleName();
            TypedQuery<T> tQuery = entityManager.createNamedQuery( className + "." + queryName, clazz);
            if (parameters != null) {
                for( Entry<String, Object> entry : parameters.entrySet() ) {
                    tQuery.setParameter(entry.getKey(), entry.getValue());
                }
            } 
            else {
                throw new QueryNotPossibleException("Queryparameter müssen angegeben werden.");
            }
                
            T result = tQuery.getSingleResult();
            return result;
        }
        catch(NoResultException e) {
            return null;
        }
    }
    
    protected String getCleanParameter(String parameter) {
        return parameter.replaceAll("%", "\\%").replaceAll("_", "\\_");
    }
            
    
    private Class getClassType() {        
        ParameterizedType parametrizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class clazz = (Class) parametrizedType.getActualTypeArguments()[0];
        
        return clazz;
    }
    
    private Map setDatabaseProperties() {
        Map properties = new HashMap();
        String url = "";
        String user = "";
        String password = "";
        
        Properties configFile = new Properties();
        try {
            configFile.load(this.getClass().getResourceAsStream("/META-INF/config.properties"));
            url = "jdbc:postgresql://" + 
                    configFile.getProperty("host") + ":" + 
                    configFile.getProperty("port") + "/" +
                    configFile.getProperty("database");
            user = configFile.getProperty("username");
            password = configFile.getProperty("password");
        }
        catch(IOException e)
        {
            //TODO: Log Exception
            System.out.println(e.getStackTrace());
        }
        // Datenbankverbindung
        properties.put(PersistenceUnitProperties.JDBC_URL, url);
        properties.put(PersistenceUnitProperties.JDBC_USER, user);
        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, password);
        
        return properties;
    }
}
