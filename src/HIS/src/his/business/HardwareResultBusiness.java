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
import his.model.providers.HardwareProvider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Stellt die Business-Klasse fuer HardwareResult dar.
 * Enthaelt Suche und {@link Collection} der gefundenen {@link Hardware}.
 * {@link HardwareDataBusiness}-Objekt kann entnommen werden.
 * @author Thomas Schulze
 */
public class HardwareResultBusiness {
    private Collection<Hardware> hardwares;
    private String lastSearchInventoryNumber;
    private String lastSearchManufacturer;
    private String lastSearchOwner;
    private String lastSearchState;
    private String lastSearchBuildIn;
    private String lastSearchPlace;
    private Date lastSearchWarrenty;
    private HardwareProvider provider;
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
    
    public HardwareDataBusiness getHardwareDataBusiness(Hardware hardware)
    {
        return new HardwareDataBusiness(hardware);
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
     * @param inventoryNumber Inventarnummer
     * @param manufacturer Hersteller
     * @param owner Besitzer
     * @param state Status
     * @param buildIn Gehoert zu
     * @param place Ort
     * @param warrenty Garantieraum
     * @return Suchergebnis in Form einer {@link Collection} mit {@link Hardware}
     */
    public Collection<Hardware> searchHardware(
            String inventoryNumber,
            String manufacturer,
            String owner,
            String state,
            String buildIn,
            String place,
            Date warrenty)
    {
        provider = new HardwareProvider();
        hardwares = new ArrayList<>();
        lastSearchInventoryNumber = inventoryNumber==null?"":inventoryNumber;
        lastSearchManufacturer = manufacturer==null?"":manufacturer;
        lastSearchOwner = owner==null?"":owner;
        lastSearchState = state==null?"":state;
        lastSearchBuildIn = buildIn==null?"":buildIn;
        lastSearchPlace = place==null?"":place;
        lastSearchWarrenty = warrenty==null?new Date():warrenty;     
        
        if(!lastSearchInventoryNumber.equals(""))
        {
            hardwares.add(provider.findById(inventoryNumber));
        }
        
        if(!lastSearchManufacturer.equals(""))
        {
            if(hardwares.size()>0)
            {
                Iterator<Hardware> it = hardwares.iterator();
                
                while(it.hasNext())
                {
                    Hardware h = it.next();
                    if(!h.getManufacturer().getName().toLowerCase().contains(lastSearchManufacturer.toLowerCase()))
                        it.remove();
                }
            }
            else
            {
                hardwares = provider.findByManufacturer(lastSearchManufacturer);
            }
        }
        
        if(!lastSearchOwner.equals(""))
        {
            if(hardwares.size()>0)
            {
                Iterator<Hardware> it = hardwares.iterator();
                
                while(it.hasNext())
                {
                    Hardware h = it.next();
                    if(!h.getOwner().getName().toLowerCase().contains(lastSearchOwner.toLowerCase()))
                        it.remove();
                }
            }
            else
            {
                 hardwares = provider.findByOwner(lastSearchOwner);
            }
        }
        
        if(!lastSearchState.equals(""))
        {
            if(hardwares.size()>0)
            {
                Iterator<Hardware> it = hardwares.iterator();
                
                while(it.hasNext())
                {
                    Hardware h = it.next();
                    if(!h.getState().getName().toLowerCase().contains(lastSearchState.toLowerCase()))
                        it.remove();
                }
            }
            else
            {
                hardwares = provider.findByState(lastSearchState);
            }
        }
        
        if(!lastSearchBuildIn.equals(""))
        {
            if(hardwares.size()>0)
            {
                Iterator<Hardware> it = hardwares.iterator();
                
                while(it.hasNext())
                {
                    Hardware h = it.next();
                    if(h.getHardware() != null 
                            && !h.getHardware().getName().toLowerCase().contains(lastSearchBuildIn.toLowerCase()))
                        it.remove();
                }                
            }
            else
            {
                 hardwares = new ArrayList<>();
                 for(Hardware h: provider.findByName(lastSearchBuildIn))
                 {
                     if(h.getHardwareCollection().contains(h))
                         hardwares.add(h);
                 }
            }
        }
        
        if(!lastSearchPlace.equals(""))
        {
            if(hardwares.size()>0)
            {
                Iterator<Hardware> it = hardwares.iterator();
                
                while(it.hasNext())
                {
                    Hardware h = it.next();
                    if(!h.getHardware().getPlace().getName().toLowerCase().contains(lastSearchPlace.toLowerCase()))
                        it.remove();
                } 
            }
            else
            {
                hardwares = provider.findByPlace(lastSearchPlace);
            }
        }
        
        if(!lastSearchWarrenty.before(Calendar.getInstance().getTime()))
        {            
            if(hardwares.size()>0)
            {
                Iterator<Hardware> it = hardwares.iterator();
                
                while(it.hasNext())
                {
                    Hardware h = it.next();
                    if(h.getWarrantyEnd() != null 
                            && !h.getWarrantyEnd().equals(lastSearchWarrenty))
                        it.remove();
                } 
            }
            else
            {
                hardwares = provider.findByWarrantyEnd(lastSearchWarrenty);
            }
        }        
        return hardwares;
    }
    
    /**
     * Fuehrt die letzte Suche nochmal aus.
     * Gibt null zurueck, wenn vorher keine Suche ausgefuehrt wurde.
     * @return {@link Collection} mit gefundener {@link Hardware}
     * @throws Exception keine Suche vorher ausgefuehrt 
     */
    public Collection<Hardware> refreshSearch()
    {        
        return searchHardware(
             lastSearchInventoryNumber,
             lastSearchManufacturer,
             lastSearchOwner,
             lastSearchState,
             lastSearchBuildIn,
             lastSearchPlace,
             lastSearchWarrenty);
    }
    
}
