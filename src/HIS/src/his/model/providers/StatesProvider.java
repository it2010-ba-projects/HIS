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
import his.model.States;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author silvio
 */
public class StatesProvider extends BaseProvider<States> {
    
    @Override
    public void update(States state) {
        // Status können nicht aktualisiert werden
    }
    
    @Override
    public void delete(States state) {
        // Status können nicht gelöscht werden
    }
    
    @Override
    public void create(States state) {
        // Status können nicht angelegt werden
    }
    
    public States findByName(String name) {
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        try {
            return findSingleResultByQueryName("findByName", parameters);
        } catch (QueryNotPossibleException ex) {
            logger.error(ex);
            return null;
        }
    }
}
