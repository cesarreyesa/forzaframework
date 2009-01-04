package org.forzaframework.core.persistance;

/**
 * @author cesarreyes
 *         Date: 16-ago-2008
 *         Time: 12:03:51
 */
public class NullExpression extends Criterion {

	protected NullExpression(String property) {
		this.property = property;
	}

	public String toString() {
		return property + " is null";
	}

}
