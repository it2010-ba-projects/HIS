/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package his.ui.events;

import java.util.EventListener;

/**
 *
 * @author Thomas
 */
public interface QuickSearchListener extends EventListener {
    public void quickSearchPerformed(QuickSearchEvent evt);
}
