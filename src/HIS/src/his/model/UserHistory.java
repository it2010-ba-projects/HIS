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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "user_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserHistory.findAll", query = "SELECT u FROM UserHistory u"),
    @NamedQuery(name = "UserHistory.findById", query = "SELECT u FROM UserHistory u WHERE u.id = :id"),
    @NamedQuery(name = "UserHistory.findByEntry", query = "SELECT u FROM UserHistory u WHERE u.entry = :entry"),
    @NamedQuery(name = "UserHistory.findByCreatedAt", query = "SELECT u FROM UserHistory u WHERE u.createdAt = :createdAt"),
    @NamedQuery(name = "UserHistory.findByUpdatedAt", query = "SELECT u FROM UserHistory u WHERE u.updatedAt = :updatedAt")})
public class UserHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Short id;
    @Column(name = "entry", length = 2147483647)
    private String entry;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinTable(name = "user_history_users", joinColumns = {
        @JoinColumn(name = "user_history_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Users> usersCollection;

    public UserHistory() {
    }

    public UserHistory(Short id) {
        this.id = id;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
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
        if (!(object instanceof UserHistory)) {
            return false;
        }
        UserHistory other = (UserHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "his.model.UserHistory[ id=" + id + " ]";
    }
    
}
