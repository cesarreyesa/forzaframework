/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.Comparator;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 15:31:18
 */
/**
 * Compares two objects by comparing a named JavaBeans property.
 * <p/>
 * Comparators only allow two objects to be compared in entierity. This provides
 * a generic way of comparing two JavaBeans by specifying their property name
 * and the appropriate values are computed dynamically at run time.
 * <p/>
 * Example of use:
 * <p/>
 * <PRE>
 * public class PersonBean {
 * private String name;
 * public PersonBean(String name) {
 * this.name = name;
 * }
 * public String getName() {
 * return name;
 * }
 * }
 * <p/>
 * Comparator c = new BeanPropertyComparator("name");
 * // returns -1
 * c.compare(new PersonBean("Alex"),new PersonBean("Bob"));
 * <p/>
 * // returns 0
 * c.compare(new PersonBean("Alex"),new PersonBean("Alex));
 * <p/>
 * // returns 1
 * c.compare(new PersonBean("Bob"),new PersonBean("Bob"));
 * </PRE>
 * <p/>
 * <B>Note</B> the <I>property</I> is the name of a JavaBeans property, and
 * according to the JavaBeans specification must start with a lower case letter.
 * So, the property <CODE>name</CODE> is translated to the method
 * <CODE>getName()</CODE> using the standard {@link java.beans.Introspector}
 * process.
 * <p/>
 * <B>Note</B> this uses the {@link java.beans.BeanInfo} class to obtain the
 * accessor method, so that provided a
 * {@link java.beans.BeanInfo} is given, it will work regardless of the name
 * of the read method.
 *
 */
public class BeanComparator implements Comparator {

    /**
     * The property name.
     * <p/>
     * Must start with a lower-case letter as per the JavaBeans specification.
     */
    private String property;

    /**
     * The alternative comparator to use.
     * <p/>
     * Allows properties to be compared with different comparators. If this is
     * not provided, uses the object's own {@link Comparable} interface if
     * it exists.
     */
    private Comparator comparator;

    private String direction;

    /**
     * Creates a new Comparator using the property defined.
     * <p/>
     * Property names must start with a lower-case letter as per the
     * JavaBeans specification. This uses {@link java.beans.Introspector introspection} to obtain
     * the property dynamically from its access method.
     * <p/>
     * The properties are compared assuming that the bean implements {@link java.lang.Comparable}.
     *
     * @param property the property name to use (starts with a lower case letter)
     */
    public BeanComparator(String property, String direction) {
        this.property = property;
        this.direction = direction;
        //this(property, );
    }

    /**
     * Creates a new Comparator using the property defined.
     * <p/>
     * Property names must start with a lower-case letter as per the
     * JavaBeans specification. This uses {@link java.beans.Introspector introspection} to obtain
     * the property dynamically from its access method.
     * <p/>
     * The properties are compared using the given comparator.
     *
     * @param property   the property name to use (starts with a lower case letter)
     * @param comparator the comparator to compare properties
     */
    public BeanComparator(String property, Comparator comparator) {
        this.property = property;
        this.comparator = comparator;
    }

    /**
     * Compares the two objects using either the given {@link java.util.Comparator} or using the {@link java.lang.Comparable} interface.
     * <p/>
     * If no {@link java.util.Comparator} is given during construction,
     * and either <CODE>o1</CODE> or <CODE>o2</CODE> implements
     * {@link java.lang.Comparable}, then it is cast to
     * {@link java.lang.Comparable} and compared with the other.
     * <p/>
     * If no {@link java.util.Comparator} is given, and neither <CODE>o1</CODE>
     * or <CODE>o2</CODE> implement {@link java.lang.Comparable} then the
     * objects are converted to a <CODE>String</CODE> using the
     * {@link java.lang.String#valueOf} method (which calls
     * {@link java.lang.Object#toString}), and standard <CODE>String</CODE>
     * comparison is performed.
     *
     * @param o1 the object to compare
     * @param o2 the object to compare
     * @return <DL>
     *         <DT>-1</DT><DD>if <CODE>o1</CODE> &lt; <CODE>o2</CODE></DD>
     *         <DT>0</DT><DD>if <CODE>o1</CODE> = <CODE>o2</CODE></DD>
     *         <DT>1</DT><DD>if <CODE>o1</CODE> &gt; <CODE>o2</CODE></DD>
     *         </DL>
     * @throws IllegalArgumentException if there is no property named
     *                                  <I>property</I> or there is a problem accessing it with the
     *                                  <CODE>PropertyDescriptor</CODE>
     */
    public int compare(Object o1, Object o2) throws IllegalArgumentException {
        // Get the value of the properties
        Object p1 = null;
        Object p2 = null;
        Class clazz = null;
        try {
            clazz = PropertyUtils.getPropertyType(o1, property);
            p1 = PropertyUtils.getProperty(o1, property);
            p2 = PropertyUtils.getProperty(o2, property);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
        if (comparator == null) {
            int i = 0;
            // try to find p1 or p2 that implements Comparator
            if(clazz.equals(String.class)){
                if(direction.toUpperCase().equals("ASC"))
                    i = p1.toString().toUpperCase().compareTo(p2.toString().toUpperCase());
                else if(direction.toUpperCase().equals("DESC"))
                    i = p2.toString().toUpperCase().compareTo(p1.toString().toUpperCase());

                return i;
            }else if (p1 instanceof Comparable) {
                if(direction.toUpperCase().equals("ASC"))
                    i = ((Comparable) p1).compareTo(p2);
                else if(direction.toUpperCase().equals("DESC"))
                    i = ((Comparable) p2).compareTo(p1);

                return i;
            } else if (p2 instanceof Comparable) {
                return ((Comparable) p2).compareTo(p1);
            } else {
                // we have no comparables; try String comparison
                String s1 = String.valueOf(p1); // calls toString safely
                String s2 = String.valueOf(p2);
                return s1.compareTo(s2); // String implements comparable
            }
        } else {
            return comparator.compare(p1, p2);
        }
    }
}
