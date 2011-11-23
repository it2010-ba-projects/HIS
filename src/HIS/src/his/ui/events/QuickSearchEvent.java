/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package his.ui.events;

import his.business.FeatureTypen;

/**
 *
 * @author Thomas
 */
public class QuickSearchEvent extends SearchEvent{
    private FeatureTypen feature;
    private Object object;
    private QuickSearchEvent(Object source){
        super(source);
    }
    
    public QuickSearchEvent(Object source, FeatureTypen typ, Object o)
    {
        this(source);
        feature = typ;
        object = o;
    }

    /**
     * @return the feature
     */
    public FeatureTypen getFeature() {
        return feature;
    }

    /**
     * @return the object
     */
    public Object getObject() {
        return object;
    }
    
}
