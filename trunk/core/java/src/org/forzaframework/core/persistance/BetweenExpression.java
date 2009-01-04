package org.forzaframework.core.persistance;

/**
 * @author cesarreyes
 *         Date: 19-ago-2008
 *         Time: 16:59:13
 */
public class BetweenExpression extends Criterion{

    private Object lo;
    private Object hi;

    protected BetweenExpression(String property, Object lo, Object hi) {
        this.property = property;
        this.lo = lo;
        this.hi = hi;
    }

    public Object getLo() {
        return lo;
    }

    public void setLo(Object lo) {
        this.lo = lo;
    }

    public Object getHi() {
        return hi;
    }

    public void setHi(Object hi) {
        this.hi = hi;
    }
}
