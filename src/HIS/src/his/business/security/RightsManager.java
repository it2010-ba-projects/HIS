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
package his.business.security;

import his.HIS;
import his.model.Groups;
import his.model.Users;
import java.util.Collection;

/**
 * Beinhaltet statische Funktionen zur Rechtepruefung fuer 
 * einzelne Benutzer
 * @author Thomas
 * 
 */
public class RightsManager {
    /**
     * Prueft, ob fuer den Benutzer das Recht vorhanden ist
     * @param user Nutzer
     * @param group zu pruefende Gruppe
     * @return true, falls Gruppe fuer Nutzer vorhanden
     */    
    public static boolean isInGroup(Users user, Groups group)
    {
        if(user.getGroupsCollection().contains(group))        
            return true;        
        
        return false;
    }
    
    /**
     * Prueft fuer den aktuellen Nutzer, ob er in der Gruppe ist
     * @param group zu Pruefende Gruppe
     * @return true, falls Gruppe vorhanden
     */
    public static boolean isInGroup(Groups group)
    {
        return isInGroup(HIS.getCurrentUser(), group);
    }
    
    /**
     * Gibt alle Rechte fuer den aktuellen Nutzer zurueck
     * @param user Nutzer
     * @return Collection mit NutzerGruppen
     */
    public static Collection<Groups> getGroups(Users user)
    {
        return user.getGroupsCollection();
    }
    
    /**
     * Gibt alle Rechte fuer den aktuellen Nuter zurueck
     * @return Array mit NutzerRechten
     */
    public static Collection<Groups> getGroups()
    {
        return getGroups(HIS.getCurrentUser());
    }
}
