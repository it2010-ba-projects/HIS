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

/**
 * Die Klasse stellt Business-Objekte fuer {@link Categories} bereit.
 * @author Thomas Schulze
 */
public class CategoryDataBusiness {
    private Categories category;

    /**
     * {@link Categories} 
     * @return die gesuchte {@link Categories}
     */
    public Categories getCategory() {
        return category;
    }
    
    /**
     * Initialisiert das Objekt mit einer gesuchten {@link Categories}
     * @param id zu suchende id
     */
    public CategoryDataBusiness(int id)
    {
        search(id);        
    }
    
    /**
     * Initalisiert das Objekt mit einer leeren {@link Categories}
     */
    public CategoryDataBusiness()
    {
        this.category = new Categories();
    }
    
    /**
     * Sucht {@link Categories} mit der angegebenen id.
     * @param id zu suchende id
     * @return gefundene {@link Categories} bzw null, wenn nicht gefunden
     */
    public Categories newSearch(int id)
    {
        search(id);
        return getCategory();
    }
    
    public boolean saveCategoryData()
    {
        //TODO: implementieren
        return true;
    }
    
    /**
     * Fuehrt die letzte Suche erneut aus.
     * @throws Exception Wenn bisher keine Suche auf diesem Objekt ausgefuehrt wurde
     */
    public void refresh()
            throws Exception
    {
        if(category!=null)
        {
           search(this.getCategory().getId());
        }
        else
        {
            throw new Exception("Kein Element vorhanden");
        }
    }
    
    /**
     * Die eigentliche Suche.
     * Setzt gefundene {@link Categories} auf this.category
     * @param id zu suchende ID
     */
    private void search(int id)
    {
        //TODO: suche
        
        this.category = new Categories();
    }
}
