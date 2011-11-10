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
        
    /**
     * Erneuert das BusinessObjekt (ohne Speicherung)
     * @param node MUSS die Root-Node sein
     */
    public void refreshDataObjectTree(DefaultMutableTreeNode node)
    {
        DefaultMutableTreeNode dummy;
        if(node.getUserObject().equals(CATEGORY_ROOT_TEXT))
        {
            //delete every parent Categorie_parent from rootNode-Childs
            for(Enumeration e = node.children(); e.hasMoreElements();)
            {
                dummy = (DefaultMutableTreeNode)e.nextElement();
                ((Categories)dummy.getUserObject()).setCategory(null);
                //update every child
                for(Enumeration en = dummy.children();en.hasMoreElements();)
                {
                    refreshNode((DefaultMutableTreeNode)en.nextElement());
                }
            }
            root = node;
        }
        else
        {
            HIS.getLogger().info("Aenderungen konnten nicht gespeichert werden, da nicht root-Node uebergeben wurde");
        }
    }

    private void refreshNode(DefaultMutableTreeNode node) 
    {
        //add node to parent
        ((Categories)node.getUserObject())
                    .setCategory(
                    ((Categories)
                        ((DefaultMutableTreeNode)node.getParent())
                        .getUserObject()
                    )                    
                );
        //update every child
        for(Enumeration e = node.children();e.hasMoreElements();)
        {
            refreshNode((DefaultMutableTreeNode)e.nextElement());
        }        
    }
    
    /**
     * speichert alle Aenderungen des Trees in die Datenbank
     * liefert nur andere Ergebnisse, wenn vorher {@link CategoryDataBusiness#refreshDataObjectTree(javax.swing.tree.DefaultMutableTreeNode) }
     * ausgefuehrt wurde
     */
    public void saveChangesFromTree()
    {        
        for(Categories cat: categories)
        {
             provider.update(cat);  
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
            //wenn der parent == null
            if(cat.getCategory() == null)
            {
                //neue TreeNode erstellen und Kategorie als Inhalt hinzufuegen
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(cat);
                //zu root hinzufuegen                
                root.add(node);                
                
                //fuer alle Child-Kategorien childNodes hinzufuegen
                for(Categories childCat : categories)
                {         
                    if(childCat.getCategory() != null && childCat.getCategory().equals(cat))                        
                        createNodes(node,childCat);
                }                
            }
        }  
        return model;
    }    
    
    private void createNodes(DefaultMutableTreeNode parent,Categories category)
    {
        //alle Child-Kategorien zu parent hinzufuegen
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(category); 
        parent.add(child);
        
        //fuer alle childs
        for(Categories cat: categories)
        {
            if(cat.getCategory()!=null &&cat.getCategory().equals(category))
            {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(cat);    
                child.add(node);
                for(Categories catChild : categories)
                {
                    if(catChild.getCategory() != null && catChild.getCategory().equals(cat))
                        createNodes(node,catChild);
                }
            }
        }
    }
}
