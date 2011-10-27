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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author silvio
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"login"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findAllActive", query = "SELECT u FROM Users u WHERE u.deleted = false"),
    @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
    @NamedQuery(name = "Users.findByFirstName", query = "SELECT u FROM Users u WHERE u.firstName LIKE :firstName"),
    @NamedQuery(name = "Users.findByLastName", query = "SELECT u FROM Users u WHERE u.lastName LIKE :lastName"),
    @NamedQuery(name = "Users.findByLogin", query = "SELECT u FROM Users u WHERE u.login = :login"),
    @NamedQuery(name = "Users.findByCreatedFrom", query = "SELECT u FROM Users u WHERE u.createdFrom LIKE :createdFrom"),
    @NamedQuery(name = "Users.findByDeleted", query = "SELECT u FROM Users u WHERE u.deleted = :deleted")})
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Short id;
    @Basic(optional = false)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Basic(optional = false)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Basic(optional = false)
    @Column(name = "login", nullable = false, length = 50)
    private String login;
    @Basic(optional = false)
    @Column(name = "password", nullable = false, length = 60)
    private String password;
    @Basic(optional = false)
    @Column(name = "created_from", nullable = false, length = 50)
    private String createdFrom;
    @Basic(optional = false)
    @Column(name = "deleted", nullable = false)
    private boolean deleted;
    @ManyToMany(mappedBy = "usersCollection", fetch = FetchType.LAZY)
    private Collection<Groups> groupsCollection;
    @ManyToMany(mappedBy = "usersCollection", fetch = FetchType.LAZY)
    private Collection<UserHistory> userHistoryCollection;

    public Users() {
    }

    public Users(Short id) {
        this.id = id;
    }

    public Users(Short id, String firstName, String lastName, String login, String password, String createdFrom, boolean deleted) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.createdFrom = createdFrom;
        this.deleted = deleted;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @XmlTransient
    public Collection<Groups> getGroupsCollection() {
        return groupsCollection;
    }

    public void setGroupsCollection(Collection<Groups> groupsCollection) {
        this.groupsCollection = groupsCollection;
    }

    @XmlTransient
    public Collection<UserHistory> getUserHistoryCollection() {
        return userHistoryCollection;
    }

    public void setUserHistoryCollection(Collection<UserHistory> userHistoryCollection) {
        this.userHistoryCollection = userHistoryCollection;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "his.model.Users[ id=" + id + " ]";
    }
    
}
