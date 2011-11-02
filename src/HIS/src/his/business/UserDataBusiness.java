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

import his.model.Groups;
import his.model.Users;
import java.util.Collection;

/**
 * Stellt die Informationen fuer alle {@link Users}-Daten bereit
 * @author Thomas Schulze
 */
public class UserDataBusiness {
    private Users user;    

    /**
     * Gibt den geuchten {@link Users} zurueck bzw null, wenn keiner gefunden
     * @return den gesuchten {@link Users}
     */
    public Users getUser() {
        return user;
    }
    
    /**
     * Initialisiert das Objekt mit einem {@link Users}
     * @param id zu suchende ID
     */
    public UserDataBusiness(int id) {
        search(id);
    }
    
    /**
     * Initialisiert das Objekt mit einem leeren {@link Users}
     */
    public UserDataBusiness()
    {
        this.user = new Users();
    }
    
    /**
     * Sucht einen neuen {@link Users} und verwirft alle Aenderungen am letzten {@link Users}
     * @param id zu suchende ID
     * @return den gefundenen {@link Users} bzw null
     */
    public Users newSearch(int id)
    {
        search(id);
        return getUser();
    }
    
    /**
     * Speichert alle Aenderungen am {@link Users}
     * @return true, wenn erfolgreich
     */
    public boolean saveUserData()
    {
        //TODO:implementieren
        return true;
    }
    
    /**
     * Fuehrt die letzte Suche erneut aus
     * @throws Exception wenn kein {@link Users} bisher vorhanden
     */
    public void refresh()
            throws Exception
    {
        if(user!=null)
        {
            search(this.getUser().getId());
        }
        else
        {
            throw new Exception("Keine Daten vorhanden");
        }
    }
    
    /**
     * Die eigentliche Suche
     * @param id zu suchende ID
     */
    private void search(int id)
    {
        this.user = new Users();
    }
}
