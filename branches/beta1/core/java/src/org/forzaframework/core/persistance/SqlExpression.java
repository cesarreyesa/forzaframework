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

/**
 * @author cesar.reyes
 *         Date: 6/11/2008
 *         Time: 11:16:00 AM
 */
public class SqlExpression extends Criterion{

    private String sql;
    private Object[] values;
    private String[] types;

    public SqlExpression(String sql) {
        this.sql = sql;
    }

    public SqlExpression(String sql, Object value, String type) {
        this.sql = sql;
        this.values = new Object[] { value };
        this.types = new String[] { type };
    }
    public SqlExpression(String sql, Object[] values, String[] types) {
        this.sql = sql;
        this.values = values;
        this.types = types;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }
}
