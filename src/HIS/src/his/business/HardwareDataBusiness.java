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

import his.model.Hardware;

/**
 * Stellt die Informationen fuer alle {@link Hardware}-Daten bereit
 * @author Thomas Schulze
 */
public class HardwareDataBusiness {
    private Hardware hardware;

    /**
     * Gibt die gesuchte {@link Hardware} zurueck bzw null, wenn keine gefunden
     * @return die gesuchte {@link Hardware}
     */
    public Hardware getHardware() {
        return hardware;
    }

    /**
     * Initialisiert das Objekt mit einer {@link Hardware}
     * @param id zu suchende ID
     */
    public HardwareDataBusiness(int id) {
        search(id);
    }
   
    /**
     * Initialisiert das Objekt mit einer leeren {@link Hardware}
     */
    public HardwareDataBusiness()
    {
        this.hardware = new Hardware();
    }
    
    /**
     * Fuehrt eine neue Suche aus
     * @param id zu suchende ID
     * @return die gefundene {@link Hardware} bzw null
     */
    public Hardware newSearch(int id)
    {
        search(id);
        return getHardware();
    }
    
    /**
     * Speichert alle geaenderten Daten der {@link Hardware}
     * @return true, wenn Speichern erfolgreich, sonst false
     */
    public boolean saveHardwareData()
    {
        //TODO: implementieren
        return true;
    }
    
    /**
     * Fuehrt die letzte Suche erneut aus
     * @throws Exception Wenn noch keine Daten vorhanden
     */
    public void refresh()
            throws Exception
    {
        if(getHardware() != null)
        {
            search(this.getHardware().getId());        
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
        this.hardware = new Hardware();
    }
}
