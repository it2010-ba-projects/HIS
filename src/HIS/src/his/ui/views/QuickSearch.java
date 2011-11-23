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
package his.ui.views;

import his.business.CategoryResultBusiness;
import his.business.FeatureTypen;
import his.business.HardwareResultBusiness;
import his.business.UserResultBusiness;
import his.business.security.Rights;
import his.business.security.RightsManager;
import his.model.Categories;
import his.model.Hardware;
import his.model.Users;
import his.model.providers.CategoriesProvider;
import his.model.providers.HardwareProvider;
import his.model.providers.UsersProvider;
import his.ui.NotEditableDefaultTableModel;
import his.ui.events.QuickSearchEvent;
import his.ui.events.QuickSearchListener;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Thomas Schulze
 */
public class QuickSearch extends javax.swing.JDialog {
    private HardwareProvider hProvider;    
    private CategoriesProvider cProvider;
    private UsersProvider uProvider;
    private HardwareResultBusiness hResBusiness;
    private CategoryResultBusiness cResBusiness;
    private UserResultBusiness uResBusiness;
    private NotEditableDefaultTableModel model;
    
    /** Creates new form QuickSearch */
    private QuickSearch(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        hProvider = new HardwareProvider();
        cProvider = new CategoriesProvider();
        
        if(RightsManager.hasRight(Rights.ADMINISTRATOR))
        {
            uProvider = new UsersProvider();
        }
    }
    
    public QuickSearch(java.awt.Frame parent, boolean modal, String searchText)
    {
        this(parent, modal);
        search(searchText);
    }

    protected javax.swing.event.EventListenerList quickSearchListenerList =
        new javax.swing.event.EventListenerList();

    public void addQuickSearchListener(QuickSearchListener listener) {
        quickSearchListenerList.add(QuickSearchListener.class, listener);
    }

    public void removeQuickSearchListener(QuickSearchListener listener) {
        quickSearchListenerList.remove(QuickSearchListener.class, listener);
    }

    private void fireQuickSearchPerformed(QuickSearchEvent evt) {
        Object[] listeners = quickSearchListenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==QuickSearchListener.class) {
                ((QuickSearchListener)listeners[i+1]).quickSearchPerformed(evt);
            }
        }
    }
    
    private void search(String searchText) {        
        model = new NotEditableDefaultTableModel();
        model.addColumn("Typ");
        model.addColumn("gefunden in Eintrag");
        model.addColumn("Name");
        tblResult.setModel(model);
        //hardware suchen
        searchAndAddHardware(searchText);
        //Kategorien suchen
        searchAndAddCategories(searchText);
        //User suchen  
        if(uProvider != null)
            searchAndAddUsers(searchText);        
    }
    
    private void searchAndAddHardware(String searchText) {
        hResBusiness = new HardwareResultBusiness();
        Collection<Hardware> names = hResBusiness.searchHardware(searchText, null, null, null, null, null, null, null);
        Collection<Hardware> inventorys = hResBusiness.searchHardware(null, searchText, null, null, null, null, null, null);
        Collection<Hardware> manufactures = hResBusiness.searchHardware(null, null, searchText, null, null, null, null, null);
        Collection<Hardware> owners = hResBusiness.searchHardware(null, null, null, searchText, null, null, null, null);
        Collection<Hardware> states = hResBusiness.searchHardware(null, null, null, null, searchText, null, null, null);
        Collection<Hardware> buildIns = hResBusiness.searchHardware(null, null, null, null, null, searchText, null, null);
        Collection<Hardware> places = hResBusiness.searchHardware(null, null, null, null, null, null, searchText, null);
        Collection<Collection<Hardware>> listToMerge = new ArrayList<>();
        
        listToMerge.add(names);
        listToMerge.add(inventorys);
        listToMerge.add(manufactures);
        listToMerge.add(owners);
        listToMerge.add(states);
        listToMerge.add(buildIns);
        listToMerge.add(places);
        Collection<Hardware> all = mergeHardware(listToMerge);
        
        for(Hardware h: all)
        {    
           if(h.getName().toLowerCase().contains(searchText.toLowerCase()))
           {
               model.addRow(new Object[]{"Hardware","Hardware selbst",h});
           }       
           else if(h.getInventoryNumber().toLowerCase().contains(searchText.toLowerCase()))
           {
               model.addRow(new Object[]{"Hardware","Inventarnummer",h});
           }
           else if(h.getManufacturer().getName().toLowerCase().contains(searchText.toLowerCase()))
           {
               model.addRow(new Object[]{"Hardware","Hersteller",h});
           } 
           else if(h.getOwner().getName().toLowerCase().contains(searchText.toLowerCase()))
           {
               model.addRow(new Object[]{"Hardware","Besitzer",h});
           }
           else if(h.getPlace().getName().toLowerCase().contains(searchText.toLowerCase()))
           {
               model.addRow(new Object[]{"Hardware","Ort",h});
           }
           else if(h.getState().getName().toLowerCase().contains(searchText.toLowerCase()))
           {
               model.addRow(new Object[]{"Hardware","Status",h});
           }
        }
    }

    private void searchAndAddCategories(String searchText) {
        cResBusiness = new CategoryResultBusiness();
        cResBusiness.searchCategories(searchText);
        for(Categories c: cResBusiness.getCategories())
        {
            model.addRow(new Object[]{"Kategorie","Name",c});
        }
    }

    private void searchAndAddUsers(String searchText) {
        uResBusiness = new UserResultBusiness();        
        Collection<Users> firstNames = uResBusiness.searchUsers(searchText, null, null, null);
        Collection<Users> lastNames = uResBusiness.searchUsers(null, searchText, null, null);
        Collection<Users> logins = uResBusiness.searchUsers(null, null, searchText, null);
        Collection<Collection<Users>> listToMerge = new ArrayList<>();
        
        listToMerge.add(firstNames);
        listToMerge.add(lastNames);
        listToMerge.add(logins);
        
        Collection<Users> all = mergeUsers(listToMerge);
        
        for(Users u: all)
        {
            if(u.getDeleted())
            {
                continue;
            }
            
            if(u.getFirstName().toLowerCase().contains(searchText.toLowerCase()))
            {
                model.addRow(new Object[]{"Benutzer","Vorname",u});
            }
            else if(u.getLastName().toLowerCase().contains(searchText.toLowerCase()))
            {
                model.addRow(new Object[]{"Benutzer","Nachname",u});
            }
            else if(u.getLogin().toLowerCase().contains(searchText.toLowerCase()))
            {
                model.addRow(new Object[]{"Benutzer","Benutzername",u});
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
        tblResult = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Schnellsuche | Ergebnisse");

        tblResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Text", "gefunden in"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblResultMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblResult);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblResultMouseClicked
        if(evt.getClickCount()==2 && tblResult.getSelectedRowCount()>0)
        {            
           Object selectedObject = tblResult.getModel()
                                .getValueAt(tblResult.getSelectedRow(),2);
           
           String selectedTyp = (String)tblResult.getModel()
                                .getValueAt(tblResult.getSelectedRow(), 0);
           
           if(selectedTyp.equals("Hardware"))
                fireQuickSearchPerformed(new QuickSearchEvent(this, FeatureTypen.HARDWARE, (Hardware)selectedObject));
           if(selectedTyp.equals("Kategorie"))
                fireQuickSearchPerformed(new QuickSearchEvent(this, FeatureTypen.CATEGORIES, (Categories)selectedObject));
           if(selectedTyp.equals("Benutzer"))
                fireQuickSearchPerformed(new QuickSearchEvent(this, FeatureTypen.USERS, (Users)selectedObject));
        }
    }//GEN-LAST:event_tblResultMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuickSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuickSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuickSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuickSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                QuickSearch dialog = new QuickSearch(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblResult;
    // End of variables declaration//GEN-END:variables

    private Collection<Hardware> mergeHardware(Collection<Collection<Hardware>> list) {
        Collection<Hardware> resultList = new ArrayList<>();
        
        for(Collection<Hardware> hList: list)
        {
             for(Hardware h: hList)
             {
                 if(!resultList.contains(h))
                 {
                     resultList.add(h);
                 }
             }
        }  
        return resultList;
    }

    private Collection<Users> mergeUsers(Collection<Collection<Users>> list) {
        Collection<Users> resultList = new ArrayList<>();
        
        for(Collection<Users> hList: list)
        {
             for(Users h: hList)
             {
                 if(!resultList.contains(h))
                 {
                     resultList.add(h);
                 }
             }
        }  
        return resultList;
    }

}
