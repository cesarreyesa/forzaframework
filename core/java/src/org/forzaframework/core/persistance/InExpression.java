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

import java.util.List;
import java.util.ArrayList;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 15:03:50
 */
public class InExpression extends Criterion {

	private String valueType;
	private String serializedValues;
	private Object[] values;

	public InExpression() {
	}

	protected InExpression(String property, Object[] values) {
		this.property = property;
		this.values = values;
	}

	public String getSerializedValues() {
		return serializedValues;
	}

	public void setSerializedValues(String serializedValues) {
		this.serializedValues = serializedValues;
	}

	public Object[] getValues() {
		if(this.serializedValues != null && this.valueType != null){
			String[] tempValues = serializedValues.split(",");
			List values = new ArrayList();
			for(String tempValue : tempValues){
				Object value = tempValue;
				if(this.valueType.equals("long")){
					value = Long.valueOf(tempValue);
				}
				else if(this.valueType.equals("integer")){
					value = Integer.valueOf(tempValue.toString());
				}
				values.add(value);
			}
			this.values = values.toArray();
		}
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

}
