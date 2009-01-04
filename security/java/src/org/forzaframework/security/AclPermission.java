package org.forzaframework.security;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.CollectionOfElements;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author cesar.reyes
 *         Date: 23/10/2008
 *         Time: 06:27:51 PM
 */
@Entity
@Table(name = "system_security_acl")
public class AclPermission  {

    private Long id;
    private User user;
    private List<Long> entities = new ArrayList<Long>();

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @CollectionOfElements
    public List<Long> getEntities() {
        return entities;
    }

    public void setEntities(List<Long> entities) {
        this.entities = entities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AclPermission that = (AclPermission) o;

        if (entities != null ? !entities.equals(that.entities) : that.entities != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (entities != null ? entities.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AclPermission{" +
                "user=" + user +
                ", entities=" + entities +
                '}';
    }
}
