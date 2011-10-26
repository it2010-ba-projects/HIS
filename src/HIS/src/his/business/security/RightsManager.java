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

/**
 * Beinhaltet statische Funktionen zur Rechtepruefung fuer 
 * einzelne Benutzer
 * @author Thomas
 * 
 */
public class RightsManager {
    /**
     * Prueft, ob fuer den Benutzer das Recht vorhanden ist
     * @param username Nutzername
     * @param right zu pruefendes Recht
     * @return true, falls Recht fuer Nutzer vorhanden
     */    
    public static boolean hasRight(String username, Right right)
    {
        return true;
    }
    
    /**
     * Prueft fuer den aktuellen Nutzer, ob das Recht vorhanden ist
     * @param right zu Pruefendes Recht
     * @return true, falls Recht vorhanden
     */
    public static boolean hasRight(Right right)
    {
        return hasRight("aktueller Nutzer", right);
    }
    
    /**
     * Gibt alle Rechte fuer den aktuellen Nutzer zurueck
     * @param username Benutzername
     * @return Array mit NutzerRechten
     */
    public static Right[] getRights(String username)
    {
        return null;
    }
    
    /**
     * Gibt alle Rechte fuer den aktuellen Nuter zurueck
     * @return Array mit NutzerRechten
     */
    public static Right[] getRights()
    {
        return getRights("aktueller Nutzer");
    }
}
