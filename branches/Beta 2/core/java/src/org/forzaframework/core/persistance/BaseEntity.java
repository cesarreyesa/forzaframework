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

import org.dom4j.Element;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 15:52:18
 */
public abstract class BaseEntity {

    public abstract void setKey(Object id);
    public abstract Object getKey();

    public Element toXml(){
        return null;
    }

    public Element toXml(String elementName){
        return null;
    }


}
