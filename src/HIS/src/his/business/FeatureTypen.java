/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package his.business;

/**
 *
 * @author Thomas
 */
public enum FeatureTypen {
    CATEGORIES("Kategorie"),
    HARDWARE("Hardware"),
    USERS("Benutzer");
    
    private String name;
    
    private FeatureTypen(String n)
    {
        this.name = n;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
