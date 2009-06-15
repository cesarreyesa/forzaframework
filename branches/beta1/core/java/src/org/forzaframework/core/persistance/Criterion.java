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

import java.io.Serializable;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 12:41:57
 */
public abstract class Criterion implements Serializable {

	private Long id;
	protected String property;
	private Criteria criteria;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Criterion criterion = (Criterion) o;

        if (criteria != null ? !criteria.equals(criterion.criteria) : criterion.criteria != null) return false;
        if (id != null ? !id.equals(criterion.id) : criterion.id != null) return false;
        if (property != null ? !property.equals(criterion.property) : criterion.property != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (property != null ? property.hashCode() : 0);
        result = 31 * result + (criteria != null ? criteria.hashCode() : 0);
        return result;
    }
}
