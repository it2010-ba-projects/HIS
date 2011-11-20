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
import his.model.providers.UsersProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Stellt die Business-Klasse fuer UserResult dar.
 * Enthaelt Suche und {@link Collection} der gefundenen {@link Users}.
 * {@link UserDataBusiness}-Objekt kann entnommen werden.
 * @author Thomas Schulze
 **/
public class UserResultBusiness {
    private Collection<Users> users;
    private String lastSearchFirstName;
    private String lastSearchLastName;
    private String lastSearchLogin;
    private Collection<Groups> lastSearchGroups;
    private UsersProvider provider;
    
    /**
     * Gibt gesuchten {@link Users} zurueck
     * @return {@link Collection} mit vorher gesuchten {@link Users}
     */
    public Collection<Users> getUsers() {
        return users;
    }
    
    /**
     * Gibt ein {@link UserDataBusiness}-Objekt mit dazugehoerigen {@link Users} zurueck
     * @param id zu suchender {@link Users}
     * @return {@link UserDataBusiness}-Objekt fuer den {@link Users}
     */
    public UserDataBusiness getUserDataBusiness(int id){        
        return new UserDataBusiness(id);
    }
    
    public UserDataBusiness getUserDataBusiness(Users user){        
        return new UserDataBusiness(user);
    }
    /**
     * Gibt ein {@link UserDataBusiness}-Objekt mit leeren {@link Users} zurueck
     * @return {@link UserDataBusiness}-Objekt mit leeren {@link Users} 
     */
    public UserDataBusiness getUserDataBusiness()
    {
        return new UserDataBusiness();
    }
    
    /**
     * Sucht anhand der Parameter {@link Users}
     * Sollten einzelne Parameter nicht benoetigt werden,
     * muessen diese als leerer {@link String}/{@link Collection} uebergeben werden
     * @param firstName Vorname
     * @param lastName Nachname
     * @param groups {@link Groups}
     * @return Suchergebnis in Form einer {@link Collection} mit {@link Users}
     */
    public Collection<Users> searchUsers(
            String firstName,
            String lastName,
            String login,
            Collection<Groups> groups)
    {        
        boolean dummyFound = false;
        provider = new UsersProvider();        
        lastSearchFirstName = firstName==null?"":firstName;
        lastSearchLastName = lastName==null?"":lastName;
        lastSearchLogin = login==null?"":login;
        lastSearchGroups = groups==null?new ArrayList<Groups>():groups;
        users = new ArrayList<>();
        
        //Vorname
        if(!lastSearchFirstName.equals(""))
        {
            users = provider.findByFirstName(lastSearchFirstName);
        }
        else
        {
            users = provider.findAll();
        } 
        
        Iterator<Users> iter = users.iterator();
        while(iter.hasNext())
        {
            Users u = iter.next();
            if(u.getDeleted())
            {
                iter.remove();
            }
        }
        
        //Nachname
        if(!lastSearchLastName.equals(""))
        {
            //wenn schon user gesucht wurden
            if(users.size()>0)
            {
                Iterator<Users> it = users.iterator();
                while(it.hasNext())
                {
                    Users u = it.next();
                    if(!u.getLastName().toLowerCase().contains(lastSearchLastName.toLowerCase()))
                        it.remove();
                }
            }
            else
            {
                users = provider.findByLastName(lastSearchLastName);
            }
        }
        
        //Login
        if(!lastSearchLogin.equals(""))
        {
           //wenn schon user gesucht wurden
            if(users.size()>0)
            {
                Iterator<Users> it = users.iterator();
                while(it.hasNext())
                {
                    Users u = it.next();
                    if(!u.getLogin().toLowerCase().contains(lastSearchLogin.toLowerCase()))
                        it.remove();
                }
            }
            else
            {   
                users.add(provider.findByLogin(lastSearchLogin));                
            }
        }
        
        //Gruppen
        if(lastSearchGroups.size() >0)
        {
            //wenn schon user gesucht wurden
            if(users.size()>0)
            {
                Iterator<Users> it = users.iterator();
                while(it.hasNext())
                {
                    Users u = it.next();
                    for(Groups group: lastSearchGroups)
                    {
                        if(!u.getGroupsCollection().contains((group)))
                                dummyFound = true;
                    }
                    
                    if(!dummyFound)
                        it.remove();
                    
                    dummyFound = false;
                }
            }
        }  
        
        return users;
    }
    
    /**
     * Fuehrt die letzte Suche nochmal aus.
     * Wirft einen Fehler, wenn vorher keine Suche ausgefuehrt wurde.
     * @return {@link Collection} mit gefundenen {@link Users}
     * @throws Exception keine Suche vorher ausgefuehrt
     */
    public Collection<Users> refreshSearch()
            throws Exception
    {
        return searchUsers(lastSearchFirstName,lastSearchLastName,lastSearchLogin,lastSearchGroups);
    }
}
