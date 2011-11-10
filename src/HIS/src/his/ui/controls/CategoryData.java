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
package his.ui.controls;

import his.business.CategoryDataBusiness;
import his.model.Categories;
import his.model.providers.CategoriesProvider;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import javax.swing.DropMode;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Franziska Staake, Thomas Schulze
 */
public final class CategoryData extends javax.swing.JPanel {

    private CategoryDataBusiness catDataBusiness;
    /** Creates new form CategoryData */
    public CategoryData() {
        initComponents();  
        catDataBusiness = new CategoryDataBusiness(); 
        treeCategories.setModel(catDataBusiness.getCategoriesTree());
        
        treeCategories.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeCategories.setDropMode(DropMode.USE_SELECTION);
        treeCategories.setDropTarget(new DropTarget(treeCategories, TransferHandler.MOVE,
                new DropTargetAdapter() {
                    @Override
                    public void drop(DropTargetDropEvent dtde) {
 
                        TreePath selectionPath = treeCategories.getSelectionPath();
                        TreePath sourcePath = selectionPath.getParentPath();
 
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath
                                .getLastPathComponent();
 
                        Point dropLocation = dtde.getLocation();
                        TreePath targetPath = treeCategories.getClosestPathForLocation(
                                dropLocation.x, dropLocation.y);
 
                        if (isDropAllowed(sourcePath, targetPath, selectedNode)) {
                            System.out.println("drop accept");
                            DefaultMutableTreeNode targetParentNode = (DefaultMutableTreeNode) targetPath
                                    .getLastPathComponent();
                            DefaultMutableTreeNode sourceParentNode = (DefaultMutableTreeNode) sourcePath
                                    .getLastPathComponent();
 
                            sourceParentNode.remove(selectedNode);
                            targetParentNode.add(selectedNode);
 
                            dtde.dropComplete(true);
                            treeCategories.updateUI();
                            catDataBusiness.refreshDataObjectTree((DefaultMutableTreeNode)treeCategories.getModel().getRoot());
                            catDataBusiness.saveChangesFromTree();
                        } else {
                            System.out.println("drop: reject");
                            dtde.rejectDrop();
                            dtde.dropComplete(false);
                        }
                    }
 
                    private boolean isDropAllowed(TreePath sourcePath,
                                                TreePath targetPath,
                                                DefaultMutableTreeNode selectedNode) 
                    {
                        if (!selectedNode.getUserObject().equals(CategoryDataBusiness.CATEGORY_ROOT_TEXT) 
                                && (((DefaultMutableTreeNode) sourcePath
                                        .getLastPathComponent()).isLeaf()
                                        || !targetPath.equals(sourcePath)) 
                                )
                        {
                            int count = targetPath.getPathCount();
                            System.out.println("TargetPathCount: " + count);
                            
                            if(sourcePath.getPathCount() != 1){
                                for(int x = count-1; x >= sourcePath.getPathCount()-1; x--){
                                    if(selectedNode == targetPath.getPathComponent(x)){
                                        return false;
                                    }else{
                                        continue;
                                    }
                                }
                            }                            
                            return true;
                        } else{
                            return false;
                        }
                    } 
                }));        
    }
    
    public Collection<Integer> getExpandedRows()
    {
        int count = treeCategories.getRowCount();
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0;i<= count;i++)
        {
            if(treeCategories.isExpanded(i))
            {
                list.add(i);
            }
        }
        
        return list;
    }
    
    public void setExpandedRows(Collection<Integer> list)
    {
        for(Integer i: list)
        {
            treeCategories.expandRow(i);
        }
        
    }
    /**
     * Ersellt den Tree neu (es wird die Datenbank-Tabelle erneut ausgelesen und
     * Aenderungen werden verworfen.
     */
    public void refreshTree()
    {   
        if(catDataBusiness.getCategoriesTree() != null)
            catDataBusiness.createCategoriesTree();
        treeCategories.setModel(catDataBusiness.getCategoriesTree());       
    }
    
    /**
     * Macht den Tree (nicht) bearbeitbar
     * @param edit false: Baum wird gesperrt fuer Aenderungen
     */
    public void setEditable(boolean edit)
    {
        treeCategories.setEditable(edit);
        treeCategories.setDragEnabled(edit);
    }
    
    public void setEditDeleteVisible(boolean visible)
    {
        txtChange.setVisible(visible);
        txtDelete.setVisible(visible);
        validate();
        repaint();
    }
    
    /**
     * Liest die aktuell selektierte {@link Categories} aus
     * @return null, wenn keine Selektion oder root
     */
    public Categories getSelectedCategory()
    {
        TreePath selectionPath = treeCategories.getSelectionPath();
 
        if(selectionPath != null)
        {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath
                                                .getLastPathComponent();
            
            if(selectedNode.getUserObject() != CategoryDataBusiness.CATEGORY_ROOT_TEXT)
                return (Categories)selectedNode.getUserObject();
        }
        
        return null;
    }

    /**
     * setzt die {@link Categories} und erweitert bis dahin die Nodes
     * @param cat zu selktierende {@link Categories}
     */
    public void setSelectedCategory(Categories cat)
    {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeCategories.getModel().getRoot();
        for(Enumeration e = root.children(); e.hasMoreElements();)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
            
            if(((Categories)child.getUserObject()).getId()== cat.getId())
            {
                expandAndSelectPath(child);
                break;
            }
            else if(e.hasMoreElements())
            {                
                for(Enumeration en = child.children();en.hasMoreElements();)
                {
                    searchAndExpandSearchedCategory((DefaultMutableTreeNode)en.nextElement(), cat);
                }
            }
        }
    }
    
    private void searchAndExpandSearchedCategory(DefaultMutableTreeNode node, Categories cat)
    {
        //already there
        if(((Categories)node.getUserObject()).getId()== cat.getId())
        {
                expandAndSelectPath(node);
                return;
        }
        //all child nodes
        for(Enumeration e = node.children();e.hasMoreElements();)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
            
            if(((Categories)child.getUserObject()).getId()== cat.getId())
            {
                expandAndSelectPath(child);
                break;
            }
            else if(child.children().hasMoreElements())
            {
                for(Enumeration en = child.children();en.hasMoreElements();)
                {
                    searchAndExpandSearchedCategory(
                                        (DefaultMutableTreeNode)en.nextElement(),
                                        cat);
                }
            }
        }
    }    
    
    private void expandAndSelectPath(DefaultMutableTreeNode child) {         
        for(int i = treeCategories.getRowCount()-1; i>=0;i--)
        {
            treeCategories.collapseRow(i);
        }
        treeCategories.setSelectionPath(new TreePath(child.getPath()));
    }
    
    /**
     * setzt die {@link Categories} und erweitert bis dahin die Nodes
     * @param id ID der zu selktierenden {@link Categories}
     */
    public void setSelectedCategory(int id)
    {
        CategoriesProvider prov = new CategoriesProvider();
        Categories cat = prov.findById(id);
        if(cat!=null)
            setSelectedCategory(cat);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        treeCategories = new javax.swing.JTree();
        txtDelete = new javax.swing.JButton();
        txtChange = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(397, 377));

        treeCategories.setDragEnabled(true);
        treeCategories.setDropMode(javax.swing.DropMode.ON);
        jScrollPane1.setViewportView(treeCategories);

        txtDelete.setText("Löschen");

        txtChange.setText("Ändern");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDelete)
                    .addComponent(txtChange))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtChange)
                        .addGap(18, 18, 18)
                        .addComponent(txtDelete))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree treeCategories;
    private javax.swing.JButton txtChange;
    private javax.swing.JButton txtDelete;
    // End of variables declaration//GEN-END:variables

}
