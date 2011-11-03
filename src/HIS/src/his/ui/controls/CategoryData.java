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

import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.DropMode;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Franziska Staake
 */
public class CategoryData extends javax.swing.JPanel {

    /** Creates new form CategoryData */
    public CategoryData() {
        initComponents();
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
 
                        System.out.println("###################");
 
                        System.out.println("srcPath: " + sourcePath);
                        System.out.println("targetPath: " + targetPath);
                        System.out.println("selectedNode: " + selectedNode);
 
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
                        } else {
                            System.out.println("drop: reject");
                            dtde.rejectDrop();
                            dtde.dropComplete(false);
                        }
                    }
 
                    private boolean isDropAllowed(TreePath sourcePath,
                            TreePath targetPath,
                            DefaultMutableTreeNode selectedNode) {
                        if (selectedNode.toString() != "JTree" &&
                                (((DefaultMutableTreeNode) sourcePath
                                .getLastPathComponent()).isLeaf()
                                || !targetPath.equals(sourcePath)) 
                                && selectedNode.toString() 
                                   != targetPath.getPathComponent(1).toString()) 
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

        treeCategories.setDragEnabled(true);
        treeCategories.setDropMode(javax.swing.DropMode.ON);
        jScrollPane1.setViewportView(treeCategories);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree treeCategories;
    // End of variables declaration//GEN-END:variables
}
