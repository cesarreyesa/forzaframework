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

package org.forzaframework.core.persistance.hibernate;

import org.forzaframework.core.persistance.*;
import org.forzaframework.core.persistance.InExpression;
import org.forzaframework.core.persistance.SimpleExpression;
import org.forzaframework.core.persistance.Criterion;
import org.forzaframework.core.persistance.NullExpression;
import org.forzaframework.core.persistance.BetweenExpression;
import org.forzaframework.util.ClassUtils;
import org.hibernate.criterion.*;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.type.Type;
import org.hibernate.Hibernate;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.Embeddable;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.ArrayList;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 12:49:23
 */
public class CriteriaTranslatorImpl {

    private List<DetachedCriteria> criterias = new ArrayList<DetachedCriteria>();

    private DetachedCriteria getCriteria(String path){
        for (DetachedCriteria criteria : criterias) {
            if(path.equals(criteria.getAlias())){
                return criteria;
            }
        }
        return null;
    }

    public void addCriteria(Class entityClass, DetachedCriteria crit, Criterion criterion) {
        if(criterion instanceof SimpleExpression){
            addSimpleExpressionCriteria(entityClass, crit, criterion);

        }else if(criterion instanceof InExpression){
            addInExpressionCriteria(entityClass, crit, criterion);

        }else if(criterion instanceof NullExpression){
            addNullExpressionCriteria(entityClass, crit, criterion);

        }else if(criterion instanceof BetweenExpression){
            addBetweenExpressionCriteria(entityClass, crit, criterion);
        }else if(criterion instanceof SqlExpression){
            addSqlExpressionCriteria(entityClass, crit, criterion);
        }
    }

    private void simpleExpressionProcessNestedPath(Class entityClass, org.hibernate.criterion.DetachedCriteria criteria, SimpleExpression se, String originalProperty){
        // employee.name.firstName
        String nestedPath = originalProperty.substring(0, originalProperty.indexOf(".")); // employee
        String property = originalProperty.substring(originalProperty.indexOf(".") + 1); // name.firstName
        String nestedProperty = null;  // name
        if(property.indexOf(".") > 0){
            nestedProperty = property.substring(0, property.indexOf("."));
        }

        DetachedCriteria nestedCriteria = getCriteria(nestedPath);
        if(nestedCriteria == null){
            nestedCriteria = criteria.createCriteria(nestedPath, nestedPath);
            criterias.add(nestedCriteria);
        }

        Class nestedPathClass = null;
        if(nestedProperty != null){
            nestedPathClass = ClassUtils.getPropertyClass(entityClass, nestedProperty);
        }

        if(property.contains(".") && (nestedPathClass != null && !ClassUtils.hasAnnotation(nestedPathClass, Embeddable.class))){
            simpleExpressionProcessNestedPath(nestedPathClass, nestedCriteria, se, property);
            return;
        }

        org.hibernate.criterion.Criterion hibernateCriterion = null;
        if(se.getOperator().equals("=")){
            hibernateCriterion = org.hibernate.criterion.Restrictions.eq(property, se.getValue());
        }
        else if(se.getOperator().equals(">")){
            hibernateCriterion = org.hibernate.criterion.Restrictions.gt(property, se.getValue());
        }
        else if(se.getOperator().equals(" like ")){
            org.hibernate.criterion.SimpleExpression expression = org.hibernate.criterion.Restrictions.like(property, se.getValue());
            if(se.getIgnoreCase()){
                expression.ignoreCase();
            }
            hibernateCriterion = expression;
        }
        nestedCriteria.add(hibernateCriterion);
    }

    private void addSimpleExpressionCriteria(Class entityClass, org.hibernate.criterion.DetachedCriteria criteria, Criterion criterion) {
        SimpleExpression se = (SimpleExpression) criterion;
        if(se.getProperty().contains(".")){
            simpleExpressionProcessNestedPath(entityClass, criteria, se, criterion.getProperty());
        }else{
            org.hibernate.criterion.Criterion hibernateCriterion = null;
            if(se.getOperator().equals("=")){
                hibernateCriterion = org.hibernate.criterion.Restrictions.eq(se.getProperty(), se.getValue());
            }
            else if(se.getOperator().equals(">")){
                hibernateCriterion = org.hibernate.criterion.Restrictions.gt(se.getProperty(), se.getValue());
            }
            else if(se.getOperator().equals(" like ")){
                org.hibernate.criterion.SimpleExpression expression = org.hibernate.criterion.Restrictions.like(se.getProperty(), se.getValue());
                if(se.getIgnoreCase()){
                    expression.ignoreCase();
                }
                hibernateCriterion = expression;
            }
            criteria.add(hibernateCriterion);
        }
    }

    private void inExpressionProcessNestedPath(Class entityClass, org.hibernate.criterion.DetachedCriteria criteria, InExpression ie, String originalProperty){
        String nestedPath = originalProperty.substring(0, originalProperty.indexOf("."));
        String property = originalProperty.substring(originalProperty.indexOf(".") + 1);

        DetachedCriteria nestedCriteria = criteria.createCriteria(nestedPath);

        if(property.contains(".")){
            inExpressionProcessNestedPath(entityClass, nestedCriteria, ie, property);
            return;
        }

        nestedCriteria.add(org.hibernate.criterion.Restrictions.in(property, ie.getValues()));
    }

    private void addInExpressionCriteria(Class entityClass, org.hibernate.criterion.DetachedCriteria criteria, Criterion criterion) {
        InExpression ie = (InExpression) criterion;
        if(ie.getProperty().contains(".")){
            inExpressionProcessNestedPath(entityClass, criteria, ie, ie.getProperty());
        }else{
            criteria.add(org.hibernate.criterion.Restrictions.in(ie.getProperty(), ie.getValues()));
        }
    }

    private void nullExpressionProcessNestedPath(Class entityClass, org.hibernate.criterion.DetachedCriteria criteria, NullExpression ne, String originalProperty){
        String nestedPath = originalProperty.substring(0, originalProperty.indexOf("."));
        String property = originalProperty.substring(originalProperty.indexOf(".") + 1);

        DetachedCriteria nestedCriteria = criteria.createCriteria(nestedPath);

        if(property.contains(".")){
            nullExpressionProcessNestedPath(entityClass, nestedCriteria, ne, property);
            return;
        }

        nestedCriteria.add(org.hibernate.criterion.Restrictions.isNull(property));
    }

    private void addNullExpressionCriteria(Class entityClass, org.hibernate.criterion.DetachedCriteria criteria, Criterion criterion) {
        NullExpression ne = (NullExpression) criterion;
        if(ne.getProperty().contains(".")){
            nullExpressionProcessNestedPath(entityClass, criteria, ne, ne.getProperty());
        }else{
            criteria.add(org.hibernate.criterion.Restrictions.isNull(ne.getProperty()));
        }
    }

    private void betweenExpressionProcessNestedPath(Class entityClass, org.hibernate.criterion.DetachedCriteria criteria, BetweenExpression between, String originalProperty){
        String nestedPath = originalProperty.substring(0, originalProperty.indexOf("."));
        String property = originalProperty.substring(originalProperty.indexOf(".") + 1);

        DetachedCriteria nestedCriteria = criteria.createCriteria(nestedPath);

        if(property.contains(".")){
            betweenExpressionProcessNestedPath(entityClass, nestedCriteria, between, property);
            return;
        }

        nestedCriteria.add(org.hibernate.criterion.Restrictions.between(property, between.getLo(), between.getHi()));
    }

    private void addBetweenExpressionCriteria(Class entityClass, org.hibernate.criterion.DetachedCriteria criteria, Criterion criterion) {
        BetweenExpression between = (BetweenExpression) criterion;
        if(between.getProperty().contains(".")){
            betweenExpressionProcessNestedPath(entityClass, criteria, between, between.getProperty());
        }else{
            criteria.add(org.hibernate.criterion.Restrictions.between(between.getProperty(), between.getLo(), between.getHi()));
        }
    }

    private void addSqlExpressionCriteria(Class entityClass, DetachedCriteria criteria, Criterion criterion) {
        SqlExpression sqlExpression = (SqlExpression) criterion;
        List<Type> types = new ArrayList<Type>();
        for (String type : sqlExpression.getTypes()) {
            if(type.equals("string")) types.add(Hibernate.STRING);
            if(type.equals("integer")) types.add(Hibernate.INTEGER);
            if(type.equals("double")) types.add(Hibernate.DOUBLE);
            if(type.equals("date")) types.add(Hibernate.DATE);
        }
        org.hibernate.criterion.Criterion criterion1 = org.hibernate.criterion.Restrictions.sqlRestriction(sqlExpression.getSql(), sqlExpression.getValues(), types.toArray(new Type[types.size()]));
        criteria.add(criterion1);
    }
}
