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
import java.util.Collection;

/**
 * Stellt die Business-Klasse fuer HardwareResult dar.
 * Enthaelt Suche und {@link Collection} der gefundenen {@link Hardware}.
 * {@link HardwareDataBusiness}-Objekt kann entnommen werden.
 * @author Thomas Schulze
 */
public class HardwareResultBusiness {
    private Collection<Hardware> hardwares;
    private String lastSearchinventarNumber;
    private String lastSearchmanufacturer;
    private String lastSearchowner;
    private String lastSearchstate;
    private String lastSearchbuildIn;
    private String lastSearchplace;
    private String lastSearchtimeSpan;

    /**
     * Gibt eine {@link Collection} der gefundenen {@link Hardware} zurueck
     * @return the {@link Hardware}
     */
    public Collection<Hardware> getHardwares() {
        return hardwares;
    }
    
    /**
     * Sucht anhand der ID eine {@link Hardware} und gibt das dazugehoerige
     * {@link HardwareDataBusiness}-Objekt zurueck
     * @param id zu suchende ID
     * @return {@link HardwareDataBusiness}-Objekt mit dazugehoeriger {@link Hardware}
     */
    public HardwareDataBusiness getHardwareDataBusiness(int id)
    {
        return new HardwareDataBusiness(id);
    }
    
    /**
     * Gibt ein {@link HardwareDataBusiness}-Objekt mit leerer {@link Hardware} zurueck
     * @return {@link HardwareDataBusiness}-Objekt mit leerer {@link Hardware}
     */
    public HardwareDataBusiness getHardwareDataBusiness()
    {
        return new HardwareDataBusiness();
    }
    
    /**
     * Sucht anhand der Parameter {@link Hardware}
     * Sollten einzelne Parameter nicht benoetigt werden,
     * muessen diese als leerer {@link String}/{@link Collection} uebergeben werden
     * @param inventarNumber Inventarnummer
     * @param manufacturer Hersteller
     * @param owner Besitzer
     * @param state Status
     * @param buildIn Gehoert zu
     * @param place Ort
     * @param timeSpan Garantieraum
     * @return Suchergebnis in Form einer {@link Collection} mit {@link Hardware}
     */
    public Collection<Hardware> searchHardware(
            String inventarNumber,
            String manufacturer,
            String owner,
            String state,
            String buildIn,
            String place,
            String timeSpan)
    {
        
        lastSearchinventarNumber = inventarNumber;
        lastSearchmanufacturer = manufacturer;
        lastSearchowner = owner;
        lastSearchstate = state;
        lastSearchbuildIn = buildIn;
        lastSearchplace = place;
        lastSearchtimeSpan = timeSpan;
        
        if(!inventarNumber.equals(""))
        {
            ;
        }
        
        if(!manufacturer.equals(""))
        {
            ;
        }
        
        if(!owner.equals(""))
        {
            ;
        }
        
        if(!state.equals(""))
        {
            ;
        }
        
        if(!buildIn.equals(""))
        {
            ;
        }
        
        if(!place.equals(""))
        {
            ;
        }
        
        if(!timeSpan.equals(""))
        {
            ;
        }
        
        //TODO: suchen
        //TODO: Ergebnis in hardwares eintragen
        
        return getHardwares();
    }
    
    /**
     * Fuehrt die letzte Suche nochmal aus.
     * Wirft einen Fehler, wenn vorher keine Suche ausgefuehrt wurde.
     * @return {@link Collection} mit gefundener {@link Hardware}
     * @throws Exception keine Suche vorher ausgefuehrt 
     */
    public Collection<Hardware> refreshSearch()
            throws Exception
    {
        if(lastSearchinventarNumber == null)
        { 
            throw new Exception("Es wurde bisher keine Suche ausgeführt"); 
        }
        
        return searchHardware(
             lastSearchinventarNumber,
             lastSearchmanufacturer,
             lastSearchowner,
             lastSearchstate,
             lastSearchbuildIn,
             lastSearchplace,
             lastSearchtimeSpan);
    }
    
}
