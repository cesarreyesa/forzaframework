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

package org.forzaframework.test.utils;

import org.forzaframework.util.NumberUtils;

import java.text.DecimalFormat;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: gabrielchulim
 * Date: Nov 4, 2008
 * Time: 11:07:46 AM
 */
public class NumberUtilsTest extends TestCase {

    /**
     * Test para redondeo a dos decimales
     *
     * @param value valor a redondear
     */
    private Double roundValueTest (Double value){
        //Redondeamos el valor orinal
        Double roundValue = NumberUtils.roundUp(value);
        //Obtenemos el redondeo como string. DecimalFormat aplica los criterios habituales de redondeo.
        String formatValue = new DecimalFormat("#,##0.00").format(value);

//        System.out.println("Valor original: " + value + " Redondeo: " + roundValue.toString() + " Formato: " + formatValue);

        Assert.assertEquals("El formato y el redondeo no son iguales", roundValue, NumberUtils.toDouble(formatValue));

        return roundValue;

    }


    public void testRoundVales(){
        //Test cuando el tercer decimal es menor a 5 (Tuncado)
        Assert.assertEquals(this.roundValueTest(0.890), 0.89);
        Assert.assertEquals(this.roundValueTest(0.891), 0.89);
        Assert.assertEquals(this.roundValueTest(0.892), 0.89);
        Assert.assertEquals(this.roundValueTest(0.893), 0.89);
        Assert.assertEquals(this.roundValueTest(0.894), 0.89);


        //Test cuando el tercel decimal es mayor o igual a 5 (Redondeo)
        Assert.assertEquals(this.roundValueTest(0.895), 0.9);
        Assert.assertEquals(this.roundValueTest(0.896), 0.9);
        Assert.assertEquals(this.roundValueTest(0.897), 0.9);
        Assert.assertEquals(this.roundValueTest(0.898), 0.9);
        Assert.assertEquals(this.roundValueTest(0.899), 0.9);

        Assert.assertEquals(this.roundValueTest(0.856), 0.86);
        Assert.assertEquals(this.roundValueTest(0.855), 0.86);
        Assert.assertEquals(this.roundValueTest(0.854), 0.85);

        //Test cuando el tercer decimal es menor a 5 y el 4o decimal es mayor a 5
        Assert.assertEquals(this.roundValueTest(0.8909), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8908), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8907), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8906), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8905), 0.89);

        Assert.assertEquals(this.roundValueTest(0.8919), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8918), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8917), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8916), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8915), 0.89);

        Assert.assertEquals(this.roundValueTest(0.8929), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8928), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8927), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8926), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8925), 0.89);

        Assert.assertEquals(this.roundValueTest(0.8939), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8938), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8937), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8936), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8935), 0.89);

        Assert.assertEquals(this.roundValueTest(0.8949), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8948), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8947), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8946), 0.89);
        Assert.assertEquals(this.roundValueTest(0.8945), 0.89);

        Assert.assertEquals(this.roundValueTest(0.89009), 0.89);
        Assert.assertEquals(this.roundValueTest(0.89008), 0.89);
        Assert.assertEquals(this.roundValueTest(0.89007), 0.89);
        Assert.assertEquals(this.roundValueTest(0.89006), 0.89);
        Assert.assertEquals(this.roundValueTest(0.89005), 0.89);



    }


}
