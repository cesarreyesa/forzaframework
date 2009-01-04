package org.forzaframework.test.core.persistance.model;

import org.hibernate.validator.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Embeddable;
import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author cesar.reyes
 *         Date: 6/11/2008
 *         Time: 10:49:45 AM
 */
@Embeddable
public class Name implements Serializable {

    private String mode;

    public Name() {
    }

    public Name(String firstName, String lastName, String secondLastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
    }

    /**
     * Nombres
     */
    private String firstName;
    /**
     * Apellido Paterno
     */
    private String lastName;
    /**
     * Apellido Materno
     */
    private String secondLastName;

    @Column(name = "first_name") @NotNull
    @NotBlank
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name") @NotNull @NotBlank
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "second_last_name")
    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public Name setMode(String mode) {
        this.mode = mode;
        return this;
    }

    @Transient
    public String getFullName(){
        String name = firstName == null ? "" : firstName;
        name += lastName == null ? "" : " " + lastName;
        name += secondLastName == null ? "" : " " + secondLastName;
        return name;
    }

    @Transient
    private String getFullNameInverse(){
        String name = lastName == null ? "" : lastName;
        name += secondLastName == null ? "" : " " + secondLastName;
        name += firstName == null ? "" : " " + firstName;
        return name;
    }

    @Transient
    public String getNameForLayout(){
    	String name;
        if (StringUtils.isNotBlank(this.getLastName())){
        	//Se usa para $ para separar el apellido paterno, materno y el nombre
        	name = this.getLastName() + "$" + this.getSecondLastName() + "$" + this.getFirstName();
        }else{
        	//Si no existe el apellido paterno se coloca en su lugar el apellido materno
        	//y dos signos $$ juntos
        	name = this.getSecondLastName() + "$$" + this.getFirstName();
        }
        return name;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name)) return false;

        Name name = (Name) o;

        if (firstName != null ? !firstName.equals(name.firstName) : name.firstName != null) return false;
        if (lastName != null ? !lastName.equals(name.lastName) : name.lastName != null) return false;
        if (secondLastName != null ? !secondLastName.equals(name.secondLastName) : name.secondLastName != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (secondLastName != null ? secondLastName.hashCode() : 0);
        return result;
    }

    public String toString() {
        if(this.mode != null){
            return toString(this.mode);
        }
        return getFullName();
    }

    public String toString(String type) {
        if("LASTNAME_NAME".equals(type)){
            return getFullNameInverse();
        }else{
            return getFullName();
        }
    }
}