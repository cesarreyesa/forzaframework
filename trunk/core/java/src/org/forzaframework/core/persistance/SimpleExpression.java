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

package org.forzaframework.core.persistance;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 12:50:11
 */
public class SimpleExpression extends Criterion {

	private Object value;
	private String valueType;
	private Boolean ignoreCase = false;
	private String operator;

	public SimpleExpression() {
	}

	protected SimpleExpression(String property, Object value, String operator) {
		this.property = property;
		this.value = value;
		this.operator = operator;
	}

	protected SimpleExpression(String property, Object value, String operator, Boolean ignoreCase) {
		this.property = property;
		this.value = value;
		this.ignoreCase = ignoreCase;
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public SimpleExpression ignoreCase() {
		ignoreCase = true;
		return this;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("value", value).
                append("valueType", valueType).
                append("ignoreCase", ignoreCase).
                append("operator", operator).
                toString();
    }
}
