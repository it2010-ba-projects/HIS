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
import his.business.HardwareDataBusiness;
import his.model.Hardware;
import his.model.providers.HardwareProvider;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Stack;
import javax.swing.DropMode;
import javax.swing.InputVerifier;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Thomas Schulze
 */
public final class HardwareCollectionTree extends javax.swing.JPanel {
    
    private Hardware hardware;
    private DefaultMutableTreeNode root;
    Collection<Hardware> hardwares;
    
    /**
     * Initialisiert das Onjekt mit einem leeren Tree
     */
    public HardwareCollectionTree() {
        initComponents();
    }
    
    /**
     * Initalisiert das Onjekt mit der anzuzeigenden {@link Hardware}
     * @param hardware anzuzeigende {@link Hardware}
     */
    public HardwareCollectionTree(Hardware hardware)
    {
        this();
        setHardwareToUse(hardware);
    }
    
    @Override
    public void setInputVerifier(InputVerifier inputVerifier)
    {
        treeHardware.setInputVerifier(inputVerifier);
    }
    
    @Override
    public InputVerifier getInputVerifier()
    {
        return treeHardware.getInputVerifier();                
    }
    
    /**
     * Gibt die angezeigte {@link Hardware} zurueck
     * @return Die angezeigte {@link Hardware}
     */
    public final Hardware getHardwareToUse() {
        return hardware;
    }

    /**
     * Die anzuzeigende {@link Hardware} kann hier gesetzt werden.
     * Es wird im Tree die Hardware mit allen darueberliegenden Parents und allen Childs
     * @param hardware Die anzuzeigende {@link Hardware}
     */
    public final void setHardwareToUse(Hardware hardware) {
        this.hardware = hardware;
        initSmallTree();
        setSelectedHardware(hardware);        
    }
    
    /**
     * @return the selectedHardware
     */
    public Hardware getSelectedHardware() {
        TreePath selectionPath = treeHardware.getSelectionPath();
 
        if(selectionPath != null)
        {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath
                                                .getLastPathComponent();
            
            if(selectedNode.getUserObject() != HardwareDataBusiness.HARDWARE_ROOT_TEXT)
                return (Hardware)selectedNode.getUserObject();
        }
        
        return null;
    }   
    
    public void setSelectedHardware(int id)
    {
        HardwareProvider prov = new HardwareProvider();
        Hardware h = prov.findById(id);
        if(h!=null)
            setSelectedHardware(h);
    }
    
    /**
     * @param selectedHardware the selectedHardware to set
     */
    private void setSelectedHardware(Hardware selectedHardware)
    {        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeHardware.getModel().getRoot();
        for(Enumeration e = node.children(); e.hasMoreElements();)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
            
            if(((Hardware)child.getUserObject()).getId()== selectedHardware.getId())
            {
                expandAndSelectPath(child);
                break;
            }
            else if(e.hasMoreElements())
            {                
                for(Enumeration en = child.children();en.hasMoreElements();)
                {
                    searchAndExpandSearchedHardware((DefaultMutableTreeNode)en.nextElement(), selectedHardware);
                }
            }
        }
    }
    
    private void searchAndExpandSearchedHardware(DefaultMutableTreeNode node, Hardware selectedHardware) 
    {       
        //already there
        if(((Hardware)node.getUserObject()).getId()== selectedHardware.getId())
        {
                expandAndSelectPath(node);
                return;
        }
        //all child nodes
        for(Enumeration e = node.children();e.hasMoreElements();)
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
            
            if(((Hardware)child.getUserObject()).getId()== selectedHardware.getId())
            {
                expandAndSelectPath(child);
                break;
            }
            else if(child.children().hasMoreElements())
            {
                for(Enumeration en = child.children();en.hasMoreElements();)
                {
                    searchAndExpandSearchedHardware(
                                        (DefaultMutableTreeNode)en.nextElement(),
                                        selectedHardware);
                }
            }
        }
    }
    
    private void expandAndSelectPath(DefaultMutableTreeNode child) {
        for(int i = treeHardware.getRowCount()-1; i>=0;i--)
        {
            treeHardware.collapseRow(i);
        }
        treeHardware.setSelectionPath(new TreePath(child.getPath()));
    }
    
    public void initSmallTree()
    {
        root = null;        
        initDragAndDrop();
        DefaultMutableTreeNode lastNode;
        Hardware parent = hardware.getHardware();
        Stack<Hardware> parents = new Stack<>();
        
        //oberste Ebene suchen        
        if(parent != null)        
            while(true)  
            {
                parents.push(parent);
                if(parent.getHardware() == null)
                {
                    root = new DefaultMutableTreeNode(parent);
                    parents.remove(parent);
                    break;               
                }              
                parent = parent.getHardware();              
            }
        
        //alle folgenden parents hinzufuegen
        if(parents.size()>0)
        {
            lastNode = new DefaultMutableTreeNode(parents.pop());
            root.add(lastNode);            
        
            for(int i = parents.size()-2;i>=0;i++)
            {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(parents.pop());
                lastNode.add(node);
            }
        }
        TreeModel model = new DefaultTreeModel(root);
        treeHardware.setModel(model);
        setChilds(hardware, root);
    }
    
    private void initDragAndDrop() {
         treeHardware.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeHardware.setDropMode(DropMode.USE_SELECTION);
        treeHardware.setDropTarget(new DropTarget(treeHardware, TransferHandler.MOVE,
                new DropTargetAdapter() {
                    @Override
                    public void drop(DropTargetDropEvent dtde) {
 
                        TreePath selectionPath = treeHardware.getSelectionPath();
                        TreePath sourcePath = selectionPath.getParentPath();
 
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath
                                .getLastPathComponent();
 
                        Point dropLocation = dtde.getLocation();
                        TreePath targetPath = treeHardware.getClosestPathForLocation(
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
                            treeHardware.updateUI();
                            //saveTreeToDataBase();
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
                        //nur die gesetzte Hardware darf bewegt werden
                        if (!selectedNode.getUserObject().equals(HardwareDataBusiness.HARDWARE_ROOT_TEXT) 
                                && (((DefaultMutableTreeNode) sourcePath
                                        .getLastPathComponent()).isLeaf()
                                        || !targetPath.equals(sourcePath))
                                && selectedNode.getUserObject().equals(hardware)
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
    
    private void setChilds(Hardware hw, DefaultMutableTreeNode parent) 
    {
        for(Hardware h: hw.getHardwareCollection())
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(h);
            parent.add(node);
            
            for(Hardware childHW: h.getHardwareCollection())
            {
                setChilds(childHW, node);
            }            
        }
    }
    
    /**
     * Macht den Tree bearbeitbar (drag & drop etc)
     * markiert die selectedHardware
     * @param edit true: es werden alle Hardwares angezeigt und Drag & Drop wird aktiviert
     */
    public void setEditable(boolean edit)
    {
        if(hardware!= null)
        {
            treeHardware.setEditable(true);
            if(edit)
            {
                Hardware selectedH = getHardwareToUse();     
                initCompleteHardwareTree();
                setSelectedHardware(selectedH);
            }
        }
    }
    
    private void initCompleteHardwareTree() {
        hardwares = (new HardwareProvider()).findAll();
        TreeModel model = new DefaultTreeModel(root);  
    
        root = new DefaultMutableTreeNode(HardwareDataBusiness.HARDWARE_ROOT_TEXT);       
              
        //fuer alle Kategorien in der Collection
        for(Hardware har: hardwares)
        {            
            //wenn der parent == null
            if(har.getHardware() == null)
            {
                //neue TreeNode erstellen und Kategorie als Inhalt hinzufuegen
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(har);
                //zu root hinzufuegen                
                root.add(node);                
                
                //fuer alle Child-Kategorien childNodes hinzufuegen
                for(Hardware childHardware : hardwares)
                {         
                    if(childHardware.getHardware() != null && childHardware.getHardware().equals(har))                        
                        createNodes(node,childHardware);
                }                
            }
        }  
        treeHardware.setModel(model);
    }   

    private void createNodes(DefaultMutableTreeNode parent,Hardware har)
    {
        //alle Child-Kategorien zu parent hinzufuegen
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(har); 
        parent.add(child);
        
        //fuer alle childs
        for(Hardware childHW: hardwares)
        {
            if(childHW.getHardware()!=null &&childHW.getHardware().equals(har))
            {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(childHW);    
                child.add(node);
                for(Hardware deepChildHW : hardwares)
                {
                    if(deepChildHW.getHardware() != null && deepChildHW.getHardware().equals(childHW))
                        createNodes(node,deepChildHW);
                }
            }
        }
    }
    
    private void saveTreeToDataBase()
    {
       refreshDataObjectTree((DefaultMutableTreeNode)treeHardware.getModel().getRoot());
       saveChangesFromTree(); 
    }
    
    private void refreshDataObjectTree(DefaultMutableTreeNode node)
    {
        DefaultMutableTreeNode dummy;
        if(node.getUserObject().equals(HardwareDataBusiness.HARDWARE_ROOT_TEXT))
        {
            //delete every parent Categorie_parent from rootNode-Childs
            for(Enumeration e = node.children(); e.hasMoreElements();)
            {
                dummy = (DefaultMutableTreeNode)e.nextElement();
                ((Hardware)dummy.getUserObject()).setHardware(null);
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
        ((Hardware)node.getUserObject())
                    .setHardware(
                    ((Hardware)
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
    
    private void saveChangesFromTree()
    {        
        HardwareProvider provider = new HardwareProvider();
        
        for(Hardware cat: hardwares)
        {
             provider.update(cat);  
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
        treeHardware = new javax.swing.JTree();

        jScrollPane1.setViewportView(treeHardware);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree treeHardware;
    // End of variables declaration//GEN-END:variables


 




    
}
