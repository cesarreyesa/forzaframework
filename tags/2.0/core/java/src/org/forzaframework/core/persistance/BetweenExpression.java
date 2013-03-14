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
