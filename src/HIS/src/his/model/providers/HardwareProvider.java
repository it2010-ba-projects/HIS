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
import his.model.Hardware;
import his.model.States;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author silvio
 */
public class HardwareProvider extends BaseProvider<Hardware> {
    
    public Hardware findByInventoryNumber(Serializable inventoryNumber) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("inventoryNumber", inventoryNumber);
        try {
            return findSingleResultByQueryName("findByInventoryNumber", parameters);
        } catch (QueryNotPossibleException ex) {
            ex.printStackTrace();
            return null;
        }        
    }
    
    public Collection<Hardware> findByName(String name) {
        Map<String, Object> parameters = new HashMap<>();
        name = getCleanParameter(name);
        parameters.put("name", "%" + name + "%");
        
        return findCollectionByQueryName("findByName", parameters);
    }
    
    public Hardware findByPurchaseDate(Date purchaseDate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("purchaseDate", purchaseDate);
        try {
            return findSingleResultByQueryName("findByPurchaseDate", parameters);
        } catch (QueryNotPossibleException ex) {
            ex.printStackTrace();
            return null;
        }        
    }
    
    public Hardware findByWarrantyEnd(Date warrantyEnd) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("warrantyEnd", warrantyEnd);
        try {
            return findSingleResultByQueryName("findByWarrantyEnd", parameters);
        } catch (QueryNotPossibleException ex) {
            ex.printStackTrace();
            return null;
        }        
    }
    
    public Hardware findByState(String stateName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("stateName", stateName);
        try {
            return findSingleResultByQueryName("findByState", parameters);
        } catch (QueryNotPossibleException ex) {
            ex.printStackTrace();
            return null;
        }        
    }
    
    public Hardware findByState(States state) {
        Hardware hardware = null;
        
        if (state != null) {            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("stateName", state.getName());
            try {
                hardware = findSingleResultByQueryName("findByState", parameters);
            } catch (QueryNotPossibleException ex) {
                ex.printStackTrace();
            }      
        }  
        
        return hardware;
    }
    
}
