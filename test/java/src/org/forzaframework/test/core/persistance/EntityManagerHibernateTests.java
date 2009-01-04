/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.test.core.persistance;

import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.core.persistance.Criteria;
import org.forzaframework.core.persistance.Restrictions;
import org.forzaframework.test.core.persistance.model.Student;
import org.forzaframework.test.core.persistance.model.Enrolment;
import org.forzaframework.test.core.persistance.model.Employee;
import org.forzaframework.util.DateUtils;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 15:09:16
 */
public class EntityManagerHibernateTests extends AbstractTransactionalDataSourceSpringContextTests {

    private EntityManager em;

    public void setEm(EntityManager em) {
        this.em = em;
    }

    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_TYPE);
        return new String[]{
            "classpath:/org/genesis/test/core/persistance/hibernate-config.xml"
        };
    }

    @Override
    protected void prepareTestInstance() throws Exception {
        super.prepareTestInstance();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void testGetEntity(){
        Student student = new Student();
        student.setStudentNumber(100);
        student.setName("Pedro Perez");
        em.save(student);

        Student test = em.get(Student.class, student.getStudentNumber());
        assertNotNull(test);
        assertEquals("Pedro Perez", test.getName());
    }

    public void testCriteriaBetween() throws Exception {
        em.save(new Student(1, "Pedro0 Perez", DateUtils.getDate("dd/MM/yyyy", "01/01/1982")));
        em.save(new Student(2, "Pedro1 Perez", DateUtils.getDate("dd/MM/yyyy", "04/01/1982")));
        em.save(new Student(3, "Pedro2 Perez", DateUtils.getDate("dd/MM/yyyy", "15/01/1982")));
        em.save(new Student(4, "Pedro3 Perez", DateUtils.getDate("dd/MM/yyyy", "20/01/1982")));

        Criteria criteria = new Criteria();
        Date startDate = DateUtils.getDate("dd/MM/yyyy", "02/01/1982");
        Date endDate = DateUtils.getDate("dd/MM/yyyy", "16/01/1982");
        criteria.add(Restrictions.between("birthDate", startDate, endDate));
        List<Student> students = em.find(Student.class, criteria);

        assertEquals(2, students.size());
    }

    public void testMultiplesNestedCriterias() throws Exception{
        Student student = new Student(1, "Pedro0 Perez", DateUtils.getDate("dd/MM/yyyy", "01/01/1982"));
        em.save(student);

        Enrolment enrolment = new Enrolment();
        enrolment.setSemester(1);
        enrolment.setYear(2008);
        enrolment.setStudent(student);
        em.save(enrolment);
//
//        student.addEnrolement(enrolment);
//        em.save(student);

        Criteria criteria = new Criteria();
        criteria.add(Restrictions.eq("enrolments.semester", 1));
        criteria.add(Restrictions.eq("enrolments.year", 2008));
        List<Student> students = em.find(Student.class, criteria);

        assertEquals(1, students.size());
    }

    public void testOrCriteria(){
        em.save(new Employee("1111", "Pedro", "Perez", "Perez"));
        em.save(new Employee("1221", "Juan", "Lopez", "Lopez"));
        em.save(new Employee("2222", "Luis", "Pe11rez", "Nada"));

        Criteria criteria = new Criteria();
        criteria.add(Restrictions.sqlRestriction("lower({alias}.first_name) like (?) or lower({alias}.last_name) like (?) or lower({alias}.code) like (?)", new Object[] { "%11%", "%11%", "%11%" }, new String[] {"string", "string", "string"}));
        List<Employee> employees = em.find(Employee.class, criteria);

        assertEquals(2, employees.size());
    }
    
}
