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
package his.business;

import his.model.Categories;
import java.util.Collection;

/**
 * Stellt die Business-Klasse fuer CategoryResult dar.
 * Enthaelt Suche und {@link Collection} der gefundenen Kategorien.
 * {@link CategoryDataBusiness}-Objekt kann entnommen werden.
 * @author Thomas Schulze
 */
public class CategoryResultBusiness {
    private Collection<Categories> categories;
    private String lastSearchName;

    /**
     * Gibt eine {@link Collection} der gefundenen {@link Categories} zurueck
     * @return {@link Collection} mit {@link Categories}
     */
    public Collection<Categories> getCategories() {
        return categories;
    }
    
    /**
     * Sucht anhand der ID eine {@link Categories} und gibt das dazugehoerige
     * {@link CategoryDataBusiness}-Objekt zurueck
     * @param id zu suchende ID
     * @return {@link CategoryDataBusiness}-Objekt mit dazugehoeriger {@link Categories}
     */
    public CategoryDataBusiness getCategoryDataBusiness(int id)
    {
        return new CategoryDataBusiness(id);
    }
    
    /**
     * Gibt ein {@link CategoryDataBusiness}-Objekt mit leerer {@link Categories} zurueck
     * @return {@link CategoryDataBusiness}-Objekt mit leerer {@link Categories}
     */
    public CategoryDataBusiness getCategoryDataBusiness()
    {
        return new CategoryDataBusiness();
    }
            
    /**
     * Sucht anhand der Parameter {@link Categories}
     * Sollten einzelne Parameter nicht benoetigt werden,
     * muessen diese als leerer {@link String}/{@link Collection} uebergeben werden
     * @param name Name
     * @return Suchergebnis in Form einer {@link Collection} mit {@link Categories}
     */
    public Collection<Categories> searchCategories(String name)
    {
        lastSearchName = name;
        
        if(name.equals(""))
        {
            ;
        }
        
        //TODO: suche
        //TODO: in categories einfuegen
        
        return getCategories();        
    }
    
    /**
     * Fuehrt die letzte Suche nochmal aus.
     * Wirft einen Fehler, wenn vorher keine Suche ausgefuehrt wurde.
     * @return {@link Collection} mit gefundener {@link Categories}
     * @throws Exception keine Suche vorher ausgefuehrt 
     */
    public Collection<Categories> refreshSearch()
            throws Exception
    {
        if(lastSearchName == null)
        { 
            throw new Exception("Es wurde bisher keine Suche ausgeführt"); 
        }
        
        return searchCategories(lastSearchName);
    }
    
}
