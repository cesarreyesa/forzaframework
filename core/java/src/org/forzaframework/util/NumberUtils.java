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
