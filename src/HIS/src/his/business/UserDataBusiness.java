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

import his.HIS;
import his.model.Users;
import his.model.providers.UsersProvider;

/**
 * Stellt die Informationen fuer alle {@link Users}-Daten bereit
 * @author Thomas Schulze
 */
public class UserDataBusiness {
    private Users user;  
    UsersProvider provider;

    /**
     * Gibt den geuchten {@link Users} zurueck bzw null, wenn keiner gefunden
     * @return den gesuchten {@link Users}
     */
    public Users getUser() {
        return user;
    }
    
    /**
     * Initialisiert das Objekt mit einem {@link Users}, wenn die Id gefunden wird
     * @param id zu suchende ID
     */
    public UserDataBusiness(int id) {
        provider = new UsersProvider();
        search(id);
    }
        
    /**
     * Initialisiert das Objekt mit einem leeren {@link Users}
     */
    public UserDataBusiness()
    {
        this(-1);
    }
    
    /**
     * Initialisiert das Objekt mit einem {@link Users}, wenn der User gefunden wird
     * @param user zu suchender User
     */
    public UserDataBusiness(Users user) 
    {
        this(user.getId());
    }
    
    /**
     * Sucht einen neuen {@link Users} und verwirft alle Aenderungen am letzten {@link Users}.
     * Wenn kein User gefunden wird, wird null zurueck gegeben
     * @param user zu suchender {@link Users}
     * @return den gefundenen {@link Users} bzw null
     */
    public Users newSearch(Users user)
    {        
        return newSearch(user.getId());
    }
    
    /**
     * Sucht einen neuen {@link Users} und verwirft alle Aenderungen am letzten {@link Users}
     * @param id zu suchende ID
     * @return den gefundenen {@link Users} bzw null
     */
    public Users newSearch(int id)
    {
        return search(id);
    }
    
    /**
     * Speichert alle Aenderungen am {@link Users}
     */
    public void saveUserData()
    {        
        provider.update(user);        
    }    
    
    
    /**
     * Fuehrt die letzte Suche erneut aus
     */
    public Users refresh()
    {
        if(user!=null)
        {
            return search(this.getUser().getId());
        }
        else
        {
            HIS.getLogger().info("Kein Nutzer vorhanden");
            HIS.getLogger().debug("Es wurde bisher kein Nutzer gesucht.");
            return null;
        }
    }
    
    /**
     * Die eigentliche Suche
     * @param id zu suchende ID
     */
    private Users search(int id)
    {         
        if(id>-1)
        {
            this.user = provider.findById(id);
        }
        else
        {
            this.user = new Users();
        }
        
        return this.user;
    }
}
