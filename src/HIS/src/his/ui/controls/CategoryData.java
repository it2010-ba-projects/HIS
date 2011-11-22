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

import his.HIS;
import his.business.CategoryDataBusiness;
import his.business.security.Rights;
import his.business.security.RightsManager;
import his.model.Categories;
import his.model.providers.CategoriesProvider;
import his.ui.events.ComponentChangedEvent;
import his.ui.events.ComponentChangedListener;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import javax.swing.DefaultCellEditor;
import javax.swing.DropMode;
import javax.swing.InputVerifier;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.text.TextAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Franziska Staake, Thomas Schulze
 */
public final class CategoryData extends javax.swing.JPanel {

    private CategoryDataBusiness catDataBusiness;
    private boolean refreshOnExpand = true;
    
    public CategoryData() {
        initComponents();  
        
        try
        {
            catDataBusiness = new CategoryDataBusiness(); 
            initTree(); 
            checkRights(); 
            setEditDeleteVisible(false);
        }
        catch(Exception ex)
        {
            HIS.getLogger().error(ex);
        }       
    }
    
    /**
     * @return the refreshOnExpand
     */
    public boolean isRefreshOnExpand() {
        return refreshOnExpand;
    }

    /**
     * (De-)Aktiviert den Refresh von Childs beim expandieren der Knoten
     * @param refreshOnExpand the refreshOnExpand to set
     */
    public void setRefreshOnExpand(boolean refreshOnExpand) {
        this.refreshOnExpand = refreshOnExpand;
    }
    
    protected javax.swing.event.EventListenerList componentChangedListenerList =
        new javax.swing.event.EventListenerList();

    public void addComponentChangedListener(ComponentChangedListener listener) {
        componentChangedListenerList.add(ComponentChangedListener.class, listener);
    }

    public void removeComponentChangedListener(ComponentChangedListener listener) {
        componentChangedListenerList.remove(ComponentChangedListener.class, listener);
    }

    private void fireComponentChanged(ComponentChangedEvent evt) {
        Object[] listeners = componentChangedListenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==ComponentChangedListener.class) {
                ((ComponentChangedListener)listeners[i+1]).componentChangedListenerPerfomed(evt);
            }
        }
    }
    
    @Override
    public InputVerifier getInputVerifier()
    {
        return treeCategories.getInputVerifier();
    }
    
    @Override
    public void setInputVerifier(InputVerifier iv)
    {
        treeCategories.setInputVerifier(iv);
    }
    
    /**
     * gibt die Liste der aktuell erweiterten Rows zurueck
     * @return Liste mit erweiterten Rows
     */
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
    
    private void checkRights()
    {
        if(RightsManager.hasRight(Rights.ADMINISTRATOR))
        {
            txtDelete.setEnabled(true);
        }
        if(RightsManager.hasRight(Rights.PURCHASE))
        {
            txtChange.setEnabled(true);
        }
        setEditable(true);
    }
    
    /**
     * setzt die erweiterten Reihen
     * @param list Liste mit erweiterten Reihen
     */
    public void setExpandedRows(Collection<Integer> list)
    {
        for(Integer i: list)
        {
            treeCategories.expandRow(i);
        }
        
    }
    
    /**
     * Ersellt den Tree neu (es wird die Datenbank-Tabelle erneut ausgelesen und
     * Aenderungen werden verworfen).
     */
    public void refreshTree()
    {   
        if(catDataBusiness.getCategoriesTree() != null)
            catDataBusiness.createCategoriesTree();
        treeCategories.setModel(catDataBusiness.getCategoriesTree()); 
        fireComponentChanged(new ComponentChangedEvent(this));
    }
    
    /**
     * Macht den Tree (nicht) bearbeitbar
     * @param edit false: Baum wird gesperrt fuer Aenderungen
     */
    public void setEditable(boolean edit)
    {
        if(edit && RightsManager.hasRight(Rights.PURCHASE))
        {
            setEditableLocal(true);
        }        
        else
        {
            setEditableLocal(false);
        }
    }
    
    private void setEditableLocal(boolean edit)
    {
        JTextField jfield = new JTextField();
        treeCategories.setEditable(edit);
        treeCategories.setDragEnabled(edit);
        jfield.setEditable(edit);
        TreeCellEditor textEditor = new DefaultCellEditor(jfield);
        
        treeCategories.setCellEditor(textEditor);
        
        jfield.addActionListener(new TextAction(null) {

            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                Categories cat = (Categories)
                                    ((DefaultMutableTreeNode)treeCategories.getSelectionPath()
                                        .getLastPathComponent())
                                        .getUserObject();
                
                if(treeCategories.getSelectionCount()>0 
                        && RightsManager.hasRight(Rights.PURCHASE)                
                        && !this.getFocusedComponent().getText().equals(cat.getName())
                        && !this.getFocusedComponent().getText().isEmpty())
                {
                    cat.setName(this.getFocusedComponent().getText());            
                    saveTreeToDataBase();                    
                }
                
                refreshTree();
            }
        });
       
    }
    
    /**
     * Setzt die Button aendern und loeschen (un-)sichtbar
     * @param visible true, wenn Buttons sichtbar sein sollen
     */
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
     * @param id ID der zu selktierenden {@link Categories}
     */
    public void setSelectedCategory(int id, boolean refresh)
    {
        CategoriesProvider prov = new CategoriesProvider();
        Categories cat = prov.findById(id);
        if(cat!=null)
            setSelectedCategory(cat, refresh);
    }
    
    /**
     * setzt die {@link Categories} und erweitert bis dahin die Nodes
     * @param cat zu selktierende {@link Categories}
     */
    public void setSelectedCategory(Categories cat, boolean refresh)
    {  
        boolean refreshDummy = this.refreshOnExpand;
        this.refreshOnExpand = refresh;
        
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
        this.refreshOnExpand = refreshDummy;
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
    
    private void expandAndSelectPath(DefaultMutableTreeNode child) 
    {            
        for(int i = treeCategories.getRowCount()-1; i>=0;i--)
        {
            treeCategories.collapseRow(i);
        }
        treeCategories.setSelectionPath(new TreePath(child.getPath()));
    }    
    
    private void saveTreeToDataBase()
    {
       catDataBusiness.refreshDataObjectTree((DefaultMutableTreeNode)treeCategories.getModel().getRoot());
                            catDataBusiness.saveChangesFromTree(); 
    }
    
    private void deleteNode()
    {
        if(treeCategories.isEditable())
        {
            Categories cat = getSelectedCategory();
            CategoriesProvider provider = new CategoriesProvider();

            if(cat!= null && RightsManager.hasRight(Rights.ADMINISTRATOR))
            {        
                if(!cat.getCategoriesCollection().isEmpty())
                {
                   JOptionPane.showMessageDialog(this, "Die Kategorie hat Unterkategorien und kann nicht gelöscht werden!");
                }
                else if(!cat.getHardwareCollection().isEmpty())
                {
                    JOptionPane.showMessageDialog(this, "Die Kategorie kann nicht gelöscht werden, da ihr Hardware zugeordnet ist!");
                }
                else if(JOptionPane.showConfirmDialog(this,
                        "Wollen Sie die Kategorie \""+cat.getName()+"\" wirklich löschen?",
                        "Kategorie löschen",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
                {
                   provider.delete(cat); 
                   this.refreshTree(); 
                }
            }
        }
        
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
        btnRefresh = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(397, 377));

        treeCategories.setDragEnabled(true);
        treeCategories.setDropMode(javax.swing.DropMode.ON);
        treeCategories.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
                treeCategoriesTreeExpanded(evt);
            }
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
            }
        });
        treeCategories.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                treeCategoriesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(treeCategories);

        txtDelete.setText("Löschen");
        txtDelete.setEnabled(false);
        txtDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDeleteActionPerformed(evt);
            }
        });

        txtChange.setText("Ändern");
        txtChange.setEnabled(false);
        txtChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChangeActionPerformed(evt);
            }
        });

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/his/ui/refresh_48.png"))); // NOI18N
        btnRefresh.setPreferredSize(new java.awt.Dimension(67, 57));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtChange, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 234, Short.MAX_VALUE)
                        .addComponent(txtChange)
                        .addGap(18, 18, 18)
                        .addComponent(txtDelete))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChangeActionPerformed
        if(treeCategories.getSelectionCount()>0 && RightsManager.hasRight(Rights.PURCHASE))
        {
            Categories cat = (Categories)
                            ((DefaultMutableTreeNode)treeCategories.getSelectionPath()
                                .getLastPathComponent())
                                .getUserObject();
            try
            {
                String result = (String) JOptionPane.showInputDialog(this,
                        "Namen von Kategorie " + cat.getName() +" editieren",
                        "Name von Kategorie ändern",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        cat.getName());
                if(!result.equals(cat.getName()) && !result.equals(""))
                {
                    cat.setName(result);            
                    saveTreeToDataBase();
                    refreshTree();
                }
            }
            catch(Exception ex)
            {
                //TODO: so is Kacke!!!
                //Fehler erwartet, nichts machen
            }
        }
    }//GEN-LAST:event_txtChangeActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refreshTree();
        fireComponentChanged(new ComponentChangedEvent(this));
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txtDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDeleteActionPerformed
        deleteNode();
    }//GEN-LAST:event_txtDeleteActionPerformed

    private void treeCategoriesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_treeCategoriesKeyPressed
         if(evt.getKeyCode() == KeyEvent.VK_DELETE)
         {
             deleteNode();
         }
    }//GEN-LAST:event_treeCategoriesKeyPressed

    private void treeCategoriesTreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_treeCategoriesTreeExpanded
        if(refreshOnExpand)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)evt.getPath().getLastPathComponent();
            if(node.getUserObject() != CategoryDataBusiness.CATEGORY_ROOT_TEXT)
            {
                Categories cat = (Categories)node.getUserObject();        
                node.removeAllChildren();        
                node = catDataBusiness.getRefreshedNode(node,cat);
            }
            else
            {
                treeCategories.setModel(catDataBusiness.getCategoriesTree());        
            }
        }
    }//GEN-LAST:event_treeCategoriesTreeExpanded
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree treeCategories;
    private javax.swing.JButton txtChange;
    private javax.swing.JButton txtDelete;
    // End of variables declaration//GEN-END:variables

    private void initTree() {
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
                            saveTreeToDataBase();
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
}
