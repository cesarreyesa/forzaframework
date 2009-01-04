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
