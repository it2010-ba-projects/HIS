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
import his.model.Categories;
import his.model.providers.CategoriesProvider;
import java.util.Collection;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryNode;

/**
 * Die Klasse stellt Business-Objekte fuer {@link Categories} bereit.
 * @author Thomas Schulze
 */
public class CategoryDataBusiness {
    public static final String CATEGORY_ROOT_TEXT = "Kategorien";
    private Collection<Categories> categories;
    private TreeModel model;
    private DefaultMutableTreeNode root;
    private Categories searchedCategory;
    private CategoriesProvider provider;

    /**
     * {@link Categories} als {@link Collection}
     * @return die gesuchte {@link Categories}
     */
    public Collection<Categories> getCategories() {       
        return categories;
    }
    
    /**
     * @return the searchedCategory
     */
    public Categories getSearchedCategory() {
        return searchedCategory;
    }
    
    /**
     * Erstellt eine TreeNode mit allen Kategorien
     */
    public CategoryDataBusiness()
    {
        createCategoriesTree();
    }
    
    /**
     * Erstellt einen TreeNode mit allen Kategorien und 
     * hinterlegt die zu selektierende Node
     * @param cat zu selektierende Node
     */
    public CategoryDataBusiness(Categories cat) 
    {
        this();
        searchedCategory = cat;
    }
    
    public Categories getRefreshedCategory(Categories cat)
    {
        return provider.findById(cat.getId());        
    }
    
    public Categories getRefreshedCategory(int id)
    {
       return provider.findById(id);
    }
      
    public DefaultMutableTreeNode getRefreshedNode(DefaultMutableTreeNode node, Categories cat)
    {
        cat = getRefreshedCategory(cat);
        
        node.setUserObject(cat);
        
        for(Categories c: cat.getCategoriesCollection())
        {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(c);
            node.add(child);
            
            createNodes(child, c);
        }
        
        return node;
    }

    private void refreshNode(DefaultMutableTreeNode node, boolean check) 
    {
        Categories cat = (Categories)node.getUserObject();
        Categories parent = check?(Categories)((DefaultMutableTreeNode)node.getParent()).getUserObject():null;
        
        if(cat.getCategory() != null && parent == null)
        {
            cat.getCategory().getCategoriesCollection().remove(cat);
            provider.update(cat.getCategory());
            cat.setCategory(null);
            provider.update(cat);
        } 
        else if(cat.getCategory() != null && cat.getCategory() != parent)
        {
            cat.getCategory().getCategoriesCollection().remove(cat);
            provider.update(cat.getCategory());
            
            parent.getCategoriesCollection().add(cat);           
            cat.setCategory(parent);
            provider.update(cat);
            provider.update(parent);                    
        }
        else if(cat.getCategory() == null && parent != null)
        {
            parent.getCategoriesCollection().add(cat);
            cat.setCategory(parent);
            
            provider.update(parent);
            provider.update(cat);
        }
        
        for(Enumeration e = node.children();e.hasMoreElements();)
        {
            refreshNode((DefaultMutableTreeNode)e.nextElement(), true);
        }        
    }
    
    /**
     * speichert alle Aenderungen des Trees in die Datenbank
     * liefert nur andere Ergebnisse, wenn vorher {@link CategoryDataBusiness#refreshDataObjectTree(javax.swing.tree.DefaultMutableTreeNode) }
     * ausgefuehrt wurde
     */
    public void saveChangesFromTree()
    {        
        for(Enumeration e = root.children(); e.hasMoreElements();)
        {
            refreshNode((DefaultMutableTreeNode)e.nextElement(), false);               
        }        
    }
    
    /**
     * Gibt den erstellten TreeNode zurueck
     * @return TreeNode mit allen TreeNodes
     */
    public TreeModel getCategoriesTree()
    {
        if(model!=null)
        {
            return model;
        }
        else
        {
            HIS.getLogger().info("Tree abgefragt, obwohl keiner erstellt");
            return null;
        }        
    }
    
    /**
     * Erstellt einen TreeNode mit allen vorhandenen {@link Categories}
     * @return Den erstellten Tree mit allen Kategorien
     */
    public final TreeModel createCategoriesTree()
    {   
        provider = new CategoriesProvider();
        categories = provider.findAll();
        root = new DefaultMutableTreeNode(CATEGORY_ROOT_TEXT);       
        model = new DefaultTreeModel(root);        
        //fuer alle Kategorien in der Collection
        for(Categories cat: categories)
        {                
            if(cat.getCategory() == null)
            {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(cat);
                root.add(node);               
                
                createNodes(node, cat);               
            }
        }  
        return model;
    }    
    
    private void createNodes(DefaultMutableTreeNode parent,Categories category)
    {
        for(Categories cat: category.getCategoriesCollection())
        {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(cat); 
            parent.add(child); 
            createNodes(child,cat);
        }
    }
}
