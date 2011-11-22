/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package his.ui.events;

/**
 *
 * @author Thomas
 */
public class CategoriesResultShowEvent extends ResultShowEvent { 
    private boolean refresh;
    
    public CategoriesResultShowEvent(Object source, boolean refresh) {
        super(source);
        this.refresh = refresh;
    }
    
    public boolean refreshTree() {
        return refresh;
    }
    
    
}
