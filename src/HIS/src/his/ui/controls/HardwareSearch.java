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
import his.business.HardwareResultBusiness;
import his.model.Categories;
import his.model.Manufacturers;
import his.model.Owners;
import his.model.Places;
import his.model.States;
import his.model.providers.CategoriesProvider;
import his.model.providers.ManufacturersProvider;
import his.model.providers.OwnersProvider;
import his.model.providers.PlacesProvider;
import his.model.providers.StatesProvider;
import his.ui.events.SearchEvent;
import his.ui.events.SearchListener;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Franziska Staake
 */
public class HardwareSearch extends javax.swing.JPanel {
    private HardwareResultBusiness hResBusiness;
    /** Creates new form HardwareSearch */
    public HardwareSearch() {
        initComponents();
        try
        {
            initComboBoxes();
        }
        catch(Exception ex)
        {
            HIS.getLogger().error(ex);
        }
    }
    
    /**
     * @return the hResBusiness
     */
    public HardwareResultBusiness gethResBusiness() {
        return hResBusiness;
    }
    
    private void initComboBoxes() {
        initCategories();
        initManufacturers();
        initOwners();
        initPlaces();
        initStates();
    }
    
    private void initCategories() 
    {
        CategoriesProvider provider = new CategoriesProvider();
        Collection<Categories> col = provider.findAll();
        DefaultComboBoxModel<Categories> model = new DefaultComboBoxModel<>();
        
        model.addElement(null);
        for(Categories c : col)
        {
            model.addElement(c);
        }
        cbCategories.setModel(model);
    }

    private void initManufacturers() 
    {
        ManufacturersProvider provider = new ManufacturersProvider();
        Collection<Manufacturers> col = provider.findAll();
        DefaultComboBoxModel<Manufacturers> model = new DefaultComboBoxModel<>();
        
        model.addElement(null);
        for(Manufacturers m : col)
        {
            model.addElement(m);
        }
        cbManufacturers.setModel(model);
    }

    private void initOwners() 
    {
        OwnersProvider provider = new OwnersProvider();
        Collection<Owners> col = provider.findAll();
        DefaultComboBoxModel<Owners> model = new DefaultComboBoxModel<>();
        
        model.addElement(null);
        for(Owners o : col)
        {
            model.addElement(o);
        }
        cbOwners.setModel(model);
        
    }

    private void initPlaces() 
    {
        PlacesProvider provider = new PlacesProvider();
        Collection<Places> col = provider.findAll();
        DefaultComboBoxModel<Places> model = new DefaultComboBoxModel<>();
        
        model.addElement(null);
        for(Places p : col)
        {
            model.addElement(p);
        }
        cbPlaces.setModel(model);
    }

    private void initStates() 
    {
        StatesProvider provider = new StatesProvider();
        Collection<States> col = provider.findAll();
        DefaultComboBoxModel<States> model = new DefaultComboBoxModel<>();
        
        model.addElement(null);
        for(States s : col)
        {
            model.addElement(s);
        }
        cbStates.setModel(model);
    }

    protected javax.swing.event.EventListenerList searchListenerList =
        new javax.swing.event.EventListenerList();
    
     /**
     * 
     * @param listener 
     */
    public void addHardwareSearchListener(SearchListener listener) {
        searchListenerList.add(SearchListener.class, listener);
    }
    
    /**
     * 
     * @param listener
     */
    public void removeHardwareSearchListener(SearchListener listener) {
        searchListenerList.remove(SearchListener.class, listener);
    }

    /**
     *
     * @param evt 
     */
    void fireHardwareSearch(SearchEvent evt) {
        Object[] listeners = searchListenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==SearchListener.class) {
                ((SearchListener)listeners[i+1]).searchPerformed(evt);
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

        jLabel1 = new javax.swing.JLabel();
        txtInventoryNumber = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbCategories = new javax.swing.JComboBox();
        cbManufacturers = new javax.swing.JComboBox();
        cbPlaces = new javax.swing.JComboBox();
        cbOwners = new javax.swing.JComboBox();
        cbStates = new javax.swing.JComboBox();

        setPreferredSize(new java.awt.Dimension(130, 300));

        jLabel1.setText("Inventarnummer");

        jLabel2.setText("Kategorie");

        jLabel3.setText("Hersteller");

        jLabel4.setText("Ort");

        jLabel5.setText("Beistzer");

        jLabel6.setText("Status");

        btnSearch.setText("Suchen");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jLabel7.setText("Name");

        cbCategories.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbManufacturers.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbPlaces.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbOwners.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbStates.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearch)
                    .addComponent(txtInventoryNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(cbCategories, 0, 110, Short.MAX_VALUE)
                    .addComponent(cbManufacturers, javax.swing.GroupLayout.Alignment.TRAILING, 0, 110, Short.MAX_VALUE)
                    .addComponent(cbPlaces, 0, 110, Short.MAX_VALUE)
                    .addComponent(cbOwners, 0, 110, Short.MAX_VALUE)
                    .addComponent(cbStates, 0, 110, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInventoryNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(4, 4, 4)
                .addComponent(cbCategories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbManufacturers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbPlaces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOwners, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbStates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String name = txtName.getText();
        String inventoryNumber = txtInventoryNumber.getText();
        String category = cbCategories.getSelectedItem()==null?null:((Categories)cbCategories.getSelectedItem()).getName();
        String place = cbPlaces.getSelectedItem()==null?null:((Places)cbPlaces.getSelectedItem()).getName();
        String owner = cbOwners.getSelectedItem()==null?null:((Owners)cbOwners.getSelectedItem()).getName();
        String manufacturer = cbManufacturers.getSelectedItem()==null?null:((Manufacturers)cbManufacturers.getSelectedItem()).getName();
        String state = cbStates.getSelectedItem()==null?null:((States)cbStates.getSelectedItem()).getName();
        hResBusiness = new HardwareResultBusiness();
        gethResBusiness().searchHardware(name, inventoryNumber, manufacturer, owner, state, null, place,category, null);
        fireHardwareSearch(new SearchEvent(this));
    }//GEN-LAST:event_btnSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox cbCategories;
    private javax.swing.JComboBox cbManufacturers;
    private javax.swing.JComboBox cbOwners;
    private javax.swing.JComboBox cbPlaces;
    private javax.swing.JComboBox cbStates;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField txtInventoryNumber;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables


}
