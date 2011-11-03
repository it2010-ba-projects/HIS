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
import his.model.Users;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author silvio
 */
public class UsersProvider extends BaseProvider<Users> {
    
    public Collection<Users> findAllActive() {
        return findCollectionByQueryName("findAllActive");
    }
    
    public Collection<Users> findAllInActive() {
        return findCollectionByQueryName("findAllInActive");
    }
    
    public Collection<Users> findByFirstName(String firstName) {
        Map<String, Object> parameters = new HashMap<>();
        firstName = getCleanParameter(firstName);        
        parameters.put("firstName", "%" + firstName + "%");
        
        return findCollectionByQueryName("findByFirstName", parameters);
    }
    
    public Collection<Users> findByLastName(String lastName) {
        Map<String, Object> parameters = new HashMap<>();
        lastName = getCleanParameter(lastName);
        parameters.put("lastName", "%" + lastName + "%");
        
        return findCollectionByQueryName("findByLastName", parameters);
    }
    
    public Collection<Users> findByCreatedFrom(String createdFrom) {
        Map<String, Object> parameters = new HashMap<>();
        createdFrom = getCleanParameter(createdFrom);
        parameters.put("createdFrom", "%" + createdFrom + "%");
        
        return findCollectionByQueryName("findByCreatedFrom", parameters);
    }
    
    public Users findByLogin(String login) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("login", login);
        try {
            return findSingleResultByQueryName("findByLogin", parameters);
        } catch (QueryNotPossibleException ex) {
            logger.error(ex);
            return null;
        }
        
    }
}
