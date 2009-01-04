package org.forzaframework.test.core.persistance.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

/**
 * @author cesar.reyes
 *         Date: 6/11/2008
 *         Time: 10:49:30 AM
 */
@Entity
@Table(name = "employee_master")
public class Employee {

    private Long id;
    private String code;
    private Name name = new Name();

    public Employee(String code, String firstName, String lastName, String secondLastName) {
        this.code = code;
        this.name = new Name(firstName, lastName, secondLastName);
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }
}
