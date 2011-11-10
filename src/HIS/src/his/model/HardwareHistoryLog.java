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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author silvio
 */
@Entity
@Table(name = "hardware_history_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HardwareHistoryLog.findAll", query = "SELECT h FROM HardwareHistoryLog h"),
    @NamedQuery(name = "HardwareHistoryLog.findById", query = "SELECT h FROM HardwareHistoryLog h WHERE h.id = :id"),
    @NamedQuery(name = "HardwareHistoryLog.findByEntry", query = "SELECT h FROM HardwareHistoryLog h WHERE h.entry = :entry")})
public class HardwareHistoryLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="SEQ_HARDWARE_HISTORY_LOG")    
    @TableGenerator(name="SEQ_HARDWARE_HISTORY_LOG", table="sequence", pkColumnName="SEQ_NAME",
        valueColumnName="SEQ_COUNT", pkColumnValue="SEQ_HARDWARE_HISTORY_LOG", allocationSize=1)
    private Short id;
    @Basic(optional = false)
    @Column(name = "entry", nullable = false, length = 2147483647)
    private String entry;
    @ManyToMany(mappedBy = "hardwareHistoryLogCollection", fetch = FetchType.LAZY)
    private Collection<Hardware> hardwareCollection;

    public HardwareHistoryLog() {
    }

    public HardwareHistoryLog(Short id) {
        this.id = id;
    }

    public HardwareHistoryLog(Short id, String entry) {
        this.id = id;
        this.entry = entry;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    @XmlTransient
    public Collection<Hardware> getHardwareCollection() {
        return hardwareCollection;
    }

    public void setHardwareCollection(Collection<Hardware> hardwareCollection) {
        this.hardwareCollection = hardwareCollection;
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
        if (!(object instanceof HardwareHistoryLog)) {
            return false;
        }
        HardwareHistoryLog other = (HardwareHistoryLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return entry;
    }
    
}
