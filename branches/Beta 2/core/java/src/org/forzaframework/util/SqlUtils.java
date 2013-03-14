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

package org.forzaframework.util;

/**
 * User: gabriel.chulim
 * Date: 26/05/11
 * Time: 10:43 AM
 */
public class SqlUtils {
    public final static int SQL_SERVER_MAX_PARAMS = 2000;

    public static Integer getFinalIndex(Integer initialIndex, Integer sqlmaxParams, Integer totalParams) {
        if ((initialIndex + sqlmaxParams) > totalParams) {
            return totalParams;
        } else {
            return initialIndex += sqlmaxParams;
        }
    }

}
