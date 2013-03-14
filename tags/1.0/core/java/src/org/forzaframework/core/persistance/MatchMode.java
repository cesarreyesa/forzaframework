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
import java.util.Map;
import java.util.HashMap;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 17:24:12
 */
public abstract class MatchMode implements Serializable {
	private final String name;
	private static final Map INSTANCES = new HashMap();

	protected MatchMode(String name) {
		this.name=name;
	}
	public String toString() {
		return name;
	}

	/**
	 * Match the entire string to the pattern
	 */
	public static final MatchMode EXACT = new MatchMode("EXACT") {
		public String toMatchString(String pattern) {
			return pattern;
		}
	};

	/**
	 * Match the start of the string to the pattern
	 */
	public static final MatchMode START = new MatchMode("START") {
		public String toMatchString(String pattern) {
			return pattern + '%';
		}
	};

	/**
	 * Match the end of the string to the pattern
	 */
	public static final MatchMode END = new MatchMode("END") {
		public String toMatchString(String pattern) {
			return '%' + pattern;
		}
	};

	/**
	 * Match the pattern anywhere in the string
	 */
	public static final MatchMode ANYWHERE = new MatchMode("ANYWHERE") {
		public String toMatchString(String pattern) {
			return '%' + pattern + '%';
		}
	};

	static {
		INSTANCES.put( EXACT.name, EXACT );
		INSTANCES.put( END.name, END );
		INSTANCES.put( START.name, START );
		INSTANCES.put( ANYWHERE.name, ANYWHERE );
	}

	private Object readResolve() {
		return INSTANCES.get(name);
	}

	/**
	 * convert the pattern, by appending/prepending "%"
	 */
	public abstract String toMatchString(String pattern);

}
