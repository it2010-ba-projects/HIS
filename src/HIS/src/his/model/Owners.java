/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package his.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author silvio
 */
@Entity
@Table(name = "owners")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Owners.findAll", query = "SELECT o FROM Owners o"),
    @NamedQuery(name = "Owners.findById", query = "SELECT o FROM Owners o WHERE o.id = :id"),
    @NamedQuery(name = "Owners.findByName", query = "SELECT o FROM Owners o WHERE o.name = :name"),
    @NamedQuery(name = "Owners.findByCreatedAt", query = "SELECT o FROM Owners o WHERE o.createdAt = :createdAt"),
    @NamedQuery(name = "Owners.findByUdpatedAt", query = "SELECT o FROM Owners o WHERE o.udpatedAt = :udpatedAt")})
public class Owners implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Short id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "udpated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date udpatedAt;
    @OneToMany(mappedBy = "ownerId", fetch = FetchType.LAZY)
    private Collection<Hardware> hardwareCollection;

    public Owners() {
    }

    public Owners(Short id) {
        this.id = id;
    }

    public Owners(Short id, String name) {
        this.id = id;
        this.name = name;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUdpatedAt() {
        return udpatedAt;
    }

    public void setUdpatedAt(Date udpatedAt) {
        this.udpatedAt = udpatedAt;
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
        if (!(object instanceof Owners)) {
            return false;
        }
        Owners other = (Owners) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "his.model.Owners[ id=" + id + " ]";
    }
    
}
