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

package org.forzaframework.core.persistance;

import java.util.Collection;
import java.lang.reflect.Type;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 17:22:44
 */
public class Restrictions {

	Restrictions(){
		// cannot be instantiated
	}

	/**
	 * Apply an "equal" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression eq(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, "=");
	}
	/**
	 * Apply a "not equal" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression ne(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, "<>");
	}
	/**
	 * Apply a "like" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression like(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, " like ");
	}
	/**
	 * Apply a "like" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression like(String propertyName, String value, MatchMode matchMode) {
		return new SimpleExpression(propertyName, matchMode.toMatchString(value), " like " );
	}
	/**
	 * A case-insensitive "like", similar to Postgres <tt>ilike</tt>
	 * operator
	 *
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
//	public static Criterion ilike(String propertyName, String value, MatchMode matchMode) {
//		throw new NotImplementedException();
//	}
	/**
	 * A case-insensitive "like", similar to Postgres <tt>ilike</tt>
	 * operator
	 *
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
//	public static Criterion ilike(String propertyName, Object value) {
//		throw new NotImplementedException();
//	}
	/**
	 * Apply a "greater than" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression gt(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, ">");
	}
	/**
	 * Apply a "less than" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression lt(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, "<");
	}
	/**
	 * Apply a "less than or equal" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression le(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, "<=");
	}
	/**
	 * Apply a "greater than or equal" constraint to the named property
	 * @param propertyName
	 * @param value
	 * @return Criterion
	 */
	public static SimpleExpression ge(String propertyName, Object value) {
		return new SimpleExpression(propertyName, value, ">=");
	}
	/**
	 * Apply a "between" constraint to the named property
	 * @param propertyName
	 * @param lo value
	 * @param hi value
	 * @return Criterion
	 */
	public static Criterion between(String propertyName, Object lo, Object hi) {
		return new BetweenExpression(propertyName, lo, hi);
	}
	/**
	 * Apply an "in" constraint to the named property
	 * @param property
	 * @param values
	 * @return Criterion
	 */
	public static Criterion in(String property, Object[] values) {
		return new InExpression(property, values);
	}
	/**
	 * Apply an "in" constraint to the named property
	 * @param propertyName
	 * @param values
	 * @return Criterion
	 */
	public static Criterion in(String propertyName, Collection values) {
		return new InExpression( propertyName, values.toArray() );
	}

    public static Criterion notIn(String property, Object[] values) {
        return new NotInExpression(property, values);
    }

    public static Criterion notIn(String propertyName, Collection values) {
        return new NotInExpression( propertyName, values.toArray() );
    }

	/**
	 * Apply an "is null" constraint to the named property
	 * @param propertyName Nombre de la Propiedad
     * @return Criterion
	 */
	public static Criterion isNull(String propertyName) {
		return new NullExpression(propertyName);
	}

	/**
	 * Apply an "equal" constraint to two properties
	 */
//	public static PropertyExpression eqProperty(String propertyName, String otherPropertyName) {
//		return new PropertyExpression(propertyName, otherPropertyName, "=");
//	}
	/**
	 * Apply a "not equal" constraint to two properties
	 */
//	public static PropertyExpression neProperty(String propertyName, String otherPropertyName) {
//		return new PropertyExpression(propertyName, otherPropertyName, "<>");
//	}
	/**
	 * Apply a "less than" constraint to two properties
	 */
//	public static PropertyExpression ltProperty(String propertyName, String otherPropertyName) {
//		return new PropertyExpression(propertyName, otherPropertyName, "<");
//	}
	/**
	 * Apply a "less than or equal" constraint to two properties
	 */
//	public static PropertyExpression leProperty(String propertyName, String otherPropertyName) {
//		return new PropertyExpression(propertyName, otherPropertyName, "<=");
//	}
	/**
	 * Apply a "greater than" constraint to two properties
	 */
//	public static PropertyExpression gtProperty(String propertyName, String otherPropertyName) {
//		return new PropertyExpression(propertyName, otherPropertyName, ">");
//	}
	/**
	 * Apply a "greater than or equal" constraint to two properties
	 */
//	public static PropertyExpression geProperty(String propertyName, String otherPropertyName) {
//		return new PropertyExpression(propertyName, otherPropertyName, ">=");
//	}
	/**
	 * Apply an "is not null" constraint to the named property
	 * @return Criterion
	 */
//	public static Criterion isNotNull(String propertyName) {
//		return new NotNullExpression(propertyName);
//	}

    /**
     * Restriccion SQL
     * @param sql Sentencia sql
     * @return Criterion
     */
    public static Criterion sqlRestriction(String sql) {
        return new SqlExpression(sql);
    }

    public static Criterion sqlRestriction(String sql, Object value, String type) {
        return new SqlExpression(sql, value, type);
    }

    public static Criterion sqlRestriction(String sql, Object[] values, String[] types) {
        return new SqlExpression(sql, values, types);
    }
}
