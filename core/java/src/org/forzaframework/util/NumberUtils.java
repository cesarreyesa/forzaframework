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

package org.forzaframework.util;

/**
 * Created by IntelliJ IDEA.
 * User: gabrielchulim
 * Date: Nov 4, 2008
 * Time: 11:05:23 AM
 */
public class NumberUtils extends org.apache.commons.lang.math.NumberUtils{

    /**
     * Aplica los criterios habituales de redondeo a partir del numero de decimales especificado
     * @param value Valor a redondear
     * @param decimals Decimales a partir del cual se aplicará el redondeo
     * @return Valor redondeado
     */
    public static Double roundUp(Double value, Integer decimals){
        Double decimalsNumber = Math.pow(10, decimals);

        return  Math.round(value * decimalsNumber)/decimalsNumber;
    }

    /**
     * Aplica los criterios habituales de redondeo para el valor especificado a partir del 2o. decimal
     * @param value Valor a redondear
     * @return Valor redondeado a 2 decimales
     */
    public static Double roundUp(Double value){
        return roundUp(value, 2);
    }
}
