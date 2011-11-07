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
package his.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author silvio
 */
@Entity
@Table(name = "hardware", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"inventory_number"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hardware.findAll", query = "SELECT h FROM Hardware h"),
    @NamedQuery(name = "Hardware.findById", query = "SELECT h FROM Hardware h WHERE h.id = :id"),
    @NamedQuery(name = "Hardware.findByInventoryNumber", query = "SELECT h FROM Hardware h WHERE h.inventoryNumber = :inventoryNumber"),
    @NamedQuery(name = "Hardware.findByName", query = "SELECT h FROM Hardware h WHERE h.name LIKE :name"),
    @NamedQuery(name = "Hardware.findByPurchaseDate", query = "SELECT h FROM Hardware h WHERE h.purchaseDate = :purchaseDate"),
    @NamedQuery(name = "Hardware.findByWarrantyEnd", query = "SELECT h FROM Hardware h WHERE h.warrantyEnd = :warrantyEnd"),
    @NamedQuery(name = "Hardware.findByState", query = "SELECT h FROM Hardware h WHERE h.state.name = :stateName")})
public class Hardware implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="SEQ_HARDWARE")
    @TableGenerator(name="SEQ_HARDWARE", table="sequence", pkColumnName="SEQ_NAME",
        valueColumnName="SEQ_COUNT", pkColumnValue="SEQ_HARDWARE", allocationSize=1)
    private Short id;
    @Basic(optional = false)
    @Column(name = "inventory_number", nullable = false, length = 50)
    private String inventoryNumber;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Basic(optional = false)
    @Column(name = "purchase_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;
    @Column(name = "warranty_end")
    @Temporal(TemporalType.DATE)
    private Date warrantyEnd;
    @JoinTable(name = "hardware_history_log_hardware", joinColumns = {
        @JoinColumn(name = "hardware_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "hardware_history_log_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<HardwareHistoryLog> hardwareHistoryLogCollection;
    @ManyToMany(mappedBy = "hardwareCollection", fetch = FetchType.LAZY)
    private Collection<Categories> categoriesCollection;
    @JoinColumn(name = "state_id", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private States state;
    @JoinColumn(name = "place_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Places place;
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Owners owner;
    @JoinColumn(name = "manufacturer_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Manufacturers manufacturer;
    @OneToMany(mappedBy = "hardware", fetch = FetchType.LAZY)
    private Collection<Hardware> hardwareCollection;
    @JoinColumn(name = "hardware_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Hardware hardware;

    public Hardware() {
    }

    public Hardware(Short id) {
        this.id = id;
    }

    public Hardware(Short id, String inventoryNumber, String name, Date purchaseDate) {
        this.id = id;
        this.inventoryNumber = inventoryNumber;
        this.name = name;
        this.purchaseDate = purchaseDate;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getWarrantyEnd() {
        return warrantyEnd;
    }

    public void setWarrantyEnd(Date warrantyEnd) {
        this.warrantyEnd = warrantyEnd;
    }

    @XmlTransient
    public Collection<HardwareHistoryLog> getHardwareHistoryLogCollection() {
        return hardwareHistoryLogCollection;
    }

    public void setHardwareHistoryLogCollection(Collection<HardwareHistoryLog> hardwareHistoryLogCollection) {
        this.hardwareHistoryLogCollection = hardwareHistoryLogCollection;
    }

    @XmlTransient
    public Collection<Categories> getCategoriesCollection() {
        return categoriesCollection;
    }

    public void setCategoriesCollection(Collection<Categories> categoriesCollection) {
        this.categoriesCollection = categoriesCollection;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public Places getPlace() {
        return place;
    }

    public void setPlace(Places place) {
        this.place = place;
    }

    public Owners getOwner() {
        return owner;
    }

    public void setOwner(Owners owner) {
        this.owner = owner;
    }

    public Manufacturers getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturers manufacturer) {
        this.manufacturer = manufacturer;
    }

    @XmlTransient
    public Collection<Hardware> getHardwareCollection() {
        return hardwareCollection;
    }

    public void setHardwareCollection(Collection<Hardware> hardwareCollection) {
        this.hardwareCollection = hardwareCollection;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hardware)) {
            return false;
        }
        Hardware other = (Hardware) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "his.model.Hardware[ id=" + id + " ]";
    }
    
}
