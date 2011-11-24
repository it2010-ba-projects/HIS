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
    private String lastSearchName;
    private String lastSearchInventoryNumber;
    private String lastSearchManufacturer;
    private String lastSearchOwner;
    private String lastSearchState;
    private String lastSearchBuildIn;
    private String lastSearchPlace;
    private String lastSearchCategory;
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
     * @param category Kategorie
     * @param warrenty Garantieraum
     * @return Suchergebnis in Form einer {@link Collection} mit {@link Hardware}
     */
    public Collection<Hardware> searchHardware(
            String name,
            String inventoryNumber,
            String manufacturer,
            String owner,
            String state,
            String buildIn,
            String place,
            String category,
            Date warrenty)
    {
        boolean init = false;
        provider = new HardwareProvider();
        hardwares = new ArrayList<>();
        lastSearchName = name==null?"":name;
        lastSearchInventoryNumber = inventoryNumber==null?"":inventoryNumber;
        lastSearchManufacturer = manufacturer==null?"":manufacturer;
        lastSearchOwner = owner==null?"":owner;
        lastSearchState = state==null?"":state;
        lastSearchBuildIn = buildIn==null?"":buildIn;
        lastSearchPlace = place==null?"":place;
        lastSearchCategory = category==null?"":category;
        Date date = new Date();
        lastSearchWarrenty = warrenty==null?date:warrenty;     
        
        //Leersuche seperat abfragen, da sonst zu grosse Datenmenge
        if(lastSearchName.equals("")
                && lastSearchInventoryNumber.equals("")
                && lastSearchManufacturer.equals("")
                && lastSearchOwner.equals("")
                && lastSearchState.equals("")
                && lastSearchBuildIn.equals("")
                && lastSearchPlace.equals("")
                && lastSearchCategory.equals("")
                && lastSearchWarrenty.equals(date))
        {
            hardwares = provider.findAll();
            init = true;
        }
        
        if(!lastSearchName.equals(""))
        {
            hardwares = provider.findByName(lastSearchName);
            init = true;
        }
        
        if(!lastSearchInventoryNumber.equals(""))
        {
            Hardware h = provider.findByInventoryNumber(inventoryNumber);
            
            if(h!=null)
                hardwares.add(provider.findByInventoryNumber(inventoryNumber));
            init = true;
        }
        
        if(!lastSearchManufacturer.equals(""))
        {
            if(hardwares.size()>0)
            {
                Iterator<Hardware> it = hardwares.iterator();
                
                while(it.hasNext())
                {
                    Hardware h = it.next();
                    if(h.getManufacturer()!= null 
                            && !h.getManufacturer().getName().toLowerCase()
                                .contains(lastSearchManufacturer.toLowerCase()))
                        it.remove();
                }
            }
            else if(!init)
            {
                hardwares = provider.findByManufacturer(lastSearchManufacturer);
                init = true;
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
            else if(!init)
            {
                 hardwares = provider.findByOwner(lastSearchOwner);
                init = true;
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
            else if(!init)
            {
                hardwares = provider.findByState(lastSearchState);
                init = true;
            }
        }
        
        if(!lastSearchCategory.equals(""))
        {
            if(hardwares.size()>0)
            {
                Iterator<Hardware> it = hardwares.iterator();
                boolean found = false;
                while(it.hasNext())
                {
                    Hardware h = it.next();
                    Iterator<Categories> iter = h.getCategoriesCollection().iterator();
                    while(iter.hasNext())
                    {
                        Categories cat = iter.next();
                        if(cat != null && cat.getName().toLowerCase().contains(lastSearchCategory.toLowerCase()))
                            found = true;                            
                    }
                    
                    if(!found)
                        it.remove();
                    
                    found = true;
                }
            }
            else if(!init)
            {
                hardwares = provider.findAll();
                Iterator<Hardware> it = hardwares.iterator();
                boolean found = false;
                while(it.hasNext())
                {
                    Hardware h = it.next();
                    Iterator<Categories> iter = h.getCategoriesCollection().iterator();
                    while(iter.hasNext())
                    {
                        Categories cat = iter.next();
                        if(cat != null && cat.getName().toLowerCase().contains(lastSearchCategory.toLowerCase()))
                            found = true;                            
                    }
                    
                    if(!found)
                        it.remove();
                    
                    found = false;
                }
                init = true;                
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
            else if(!init)
            {                 
                 for(Hardware h: provider.findByName(lastSearchBuildIn))
                 {
                     if(h.getHardwareCollection().contains(h))
                         hardwares.add(h);
                 }
                init = true;
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
                    if(!h.getPlace().getName().toLowerCase().contains(lastSearchPlace.toLowerCase()))
                        it.remove();
                } 
            }
            else if(!init)
            {
                hardwares = provider.findByPlace(lastSearchPlace);
                init = true;
            }
        }
        Calendar before = Calendar.getInstance();
        before.add(Calendar.HOUR, 1);
        if(!lastSearchWarrenty.before(before.getTime()))
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
            else if(!init)
            {
                hardwares = provider.findByWarrantyEnd(lastSearchWarrenty);
                init = true;
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
             lastSearchName,
             lastSearchInventoryNumber,
             lastSearchManufacturer,
             lastSearchOwner,
             lastSearchState,
             lastSearchBuildIn,
             lastSearchPlace,
             lastSearchCategory,
             lastSearchWarrenty);
    }
    
}
