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
import his.model.Hardware;
import his.model.providers.HardwareProvider;

/**
 * Stellt die Informationen fuer alle {@link Hardware}-Daten bereit
 * @author Thomas Schulze
 */
public class HardwareDataBusiness {
    public static String HARDWARE_ROOT_TEXT = "Hardware";
    private Hardware hardware;
    private HardwareProvider provider;

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
        provider = new HardwareProvider();
        search(id);
    }
    
    /**
     * Initialisiert das Objekt mit einer {@link Hardware}
     * @param hardware zu suchende hardware
     */
    public HardwareDataBusiness(Hardware hardware)
    {
        this(hardware.getId());
    }
   
    /**
     * Initialisiert das Objekt mit einer leeren {@link Hardware}
     */
    public HardwareDataBusiness()
    {
        this(-1);
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
     */
    public void saveHardwareData()
    {
        provider.update(hardware);
    }
    
    /**
     * Fuehrt die letzte Suche erneut aus
     */
    public Hardware refresh()
    {
        if(getHardware() != null)
        {
            return search(this.getHardware().getId());        
        }
        else
        {
            HIS.getLogger().info("Keine Hardware vorhanden");
            HIS.getLogger().debug("Es wurde bisher keine Hardware gesucht.");
            return null;
        }
    }
    
    /**
     * Die eigentliche Suche
     * @param id zu suchende ID
     */
    private Hardware search(int id)
    {
        if(id>-1)
        {
            this.hardware = provider.findById(id);
        }
        else
        {
            this.hardware = new Hardware();
        }
        
        return hardware;
    }
}
