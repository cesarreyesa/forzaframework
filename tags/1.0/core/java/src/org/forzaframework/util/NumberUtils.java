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

import org.apache.commons.lang.*;
import org.apache.commons.lang.StringUtils;

import java.text.NumberFormat;
import java.util.Locale;

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

    /**
     * Convierte la cantidad pasada como parametro a palabras. Ej: 1559 se convierte a MIL QUINUENTOS CINCUENTA Y NUEVE
     *
     * @param amount    Cantidad a convertir a palabras
     * @return          Cantidad en palabras
     */
    static public String converterNumberToWords(Integer amount){
        switch(amount){
            case 0: return "CERO";
            case 1: return "UN"; //UNO
            case 2: return "DOS";
            case 3: return "TRES";
            case 4: return "CUATRO";
            case 5: return "CINCO";
            case 6: return "SEIS";
            case 7: return "SIETE";
            case 8: return "OCHO";
            case 9: return "NUEVE";
            case 10: return "DIEZ";
            case 11: return "ONCE";
            case 12: return "DOCE";
            case 13: return "TRECE";
            case 14: return "CATORCE";
            case 15: return "QUINCE";
            case 20: return "VEINTE";
            case 30: return "TREINTA";
            case 40: return "CUARENTA";
            case 50: return "CINCUENTA";
            case 60: return "SESENTA";
            case 70: return "SETENTA";
            case 80: return "OCHENTA";
            case 90: return "NOVENTA";
            case 100: return "CIEN";

            case 200: return "DOSCIENTOS";
            case 300: return "TRESCIENTOS";
            case 400: return "CUATROCIENTOS";
            case 500: return "QUINIENTOS";
            case 600: return "SEISCIENTOS";
            case 700: return "SETECIENTOS";
            case 800: return "OCHOCIENTOS";
            case 900: return "NOVECIENTOS";

            case 1000: return "UN MIL";
        }
        if(amount<20){
            //System.out.println(">15");
            return "DIECI"+ converterNumberToWords(amount-10);
        }
        if(amount<30){
            //System.out.println(">20");
            return "VEINTI" + converterNumberToWords(amount-20);
        }
        if(amount<100){
            //System.out.println("<100");
            return converterNumberToWords( (int)(amount/10)*10 ) + " Y " + converterNumberToWords(amount%10);
        }
        if(amount<200){
            //System.out.println("<200");
            return "CIENTO " + converterNumberToWords( amount - 100 );
        }
        if(amount<1000){
            //System.out.println("<1000");
            return converterNumberToWords( (int)(amount/100)*100 ) + " " + converterNumberToWords(amount%100);
        }
        if(amount<2000){
            //System.out.println("<2000");
            return "UN MIL " + converterNumberToWords( amount % 1000 );
        }
        if(amount<1000000){
            String var="";
            var = converterNumberToWords((int)(amount/1000)) + " MIL" ;
            if(amount % 1000!=0){
                var += " " + converterNumberToWords(amount % 1000);
            }
            return var;
        }

        //Revisamos si la cantidad es en millones
        if (amount >= 1000000 && amount <= Integer.MAX_VALUE){
            String millionWord;

            //Dividimos la cantidad entre 1000000 para verificar si debemos escribir la palabra millon o millones
            int millionValue = amount / 1000000;
            if(millionValue > 1){
                millionWord = " MILLONES ";
            }else{
                millionWord = " MILLON ";
            }

            int residue = amount % 1000000;
            //Si la cantidad es un multiplo de 1000000 entonces no usamos converterNumberToWords(residue) y asi eliminamos la
            //palabra "CERO" que se coloca al final
            if (residue == 0) {
                return converterNumberToWords(millionValue) + millionWord;
            }else{
                return converterNumberToWords(millionValue) + millionWord + converterNumberToWords( amount % 1000000 );
            }

        }

        return "";
    }

    /**
     * Convierte una cantidad dada a palabras.
     * Ej: La cantidad 1525.8569 se convierte a UN MIL QUINIENTOS VEINTICINCO PESOS 86/100 M. N., la palabra "PESOS" se
     * puede cambiarse a travŽs de el parametro currencyName.  
     *
     * @param amount        Cantidad a convertir en palabras
     * @param currencyName  Nombre de la moneda
     * @return              Cantidad convertida a palabaras
     */
    static public String converterNumberToWords(Double amount, String currencyName){
        //Creamos la instanacia para la clase NumberFormat y le decimos que por defaul tome como idioma US
        // para que tome como separador de decimales la coma
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        //Decimos que solamente queremos dos digitos para la parte decimal
        numberFormat.setMaximumFractionDigits(2);
        //Convertimos la cantidad a string con el formato adecuado
        String amountToString  = numberFormat.format(amount);

        // Removemos el "." para obtener un array con dos elementos, el primer
        // elemento es la parte entera de la cantidad y el segundo sera la parte decimal
        String amountArray[] = StringUtils.split(amountToString, ".");
        String decimalToString;
        //Revisamos si existe la parte decimal en el array ya que si tenemos una cantidad con
        // decimales en 0 NumberFormat nos devolvera solamente la parte entera
        if (amountArray.length > 1) {
            //Obtenemos el decimal del array
            decimalToString = amountArray[1];
        } else {
            decimalToString = "00";
        }

        //Revisamos si tenemos mas de un decimal
        if (decimalToString.length() > 1) {
            return converterNumberToWords(amount.intValue()).toLowerCase() + " " + currencyName.toLowerCase() + " " + decimalToString +"/100 M. N.";
        } else {
            return converterNumberToWords(amount.intValue()).toLowerCase() + " " + currencyName.toLowerCase() + " " + decimalToString +"0/100 M. N.";
        }
    }
}
