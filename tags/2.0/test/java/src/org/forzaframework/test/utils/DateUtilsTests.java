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

import junit.framework.TestCase;

import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;

import org.forzaframework.util.DateUtils;

/**
 * @author cesar.reyes
 *         Date: 22/10/2008
 *         Time: 05:42:13 PM
 */
public class DateUtilsTests extends TestCase {

    public void testDateUtils() throws ParseException {
        Date date = DateUtils.getDate("dd/MM/yyyy", "23/06/2009");
        assertEquals(date, DateUtils.createCalendar(2009, Calendar.JUNE, 23).getTime());
    }

    public void testCuteDate(){
        Date now = new Date();
        assertEquals("Hace 2 dia(s)", DateUtils.toCuteDate(DateUtils.addDays(now, -2)));
        assertEquals("Hace 2 mes(es)", DateUtils.toCuteDate(DateUtils.addMonths(now, -2)));
        assertEquals("Hace 2 año(s)", DateUtils.toCuteDate(DateUtils.addYears(now, -2)));
        assertEquals("Hace 3 hora(s)", DateUtils.toCuteDate(DateUtils.addHours(now, -3)));
        assertEquals("Hace 15 minuto(s)", DateUtils.toCuteDate(DateUtils.addMinutes(now, -15)));
        assertEquals("Hace unos pocos minutos", DateUtils.toCuteDate(DateUtils.addMinutes(now, -5)));
    }

    public void testGetMonthsDifference(){
        assertEquals(1, DateUtils.getMonthsDifference(DateUtils.getDate("2008/07/05"), DateUtils.getDate("2008/06/01")).intValue());
        assertEquals(5, DateUtils.getMonthsDifference(DateUtils.getDate("2008/07/14"), DateUtils.getDate("2008/02/01")).intValue());
    }
}
