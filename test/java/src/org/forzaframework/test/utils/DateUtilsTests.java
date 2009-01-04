package org.forzaframework.test.utils;

import junit.framework.TestCase;

import java.util.Date;
import java.util.Calendar;

import org.forzaframework.util.DateUtils;

/**
 * @author cesar.reyes
 *         Date: 22/10/2008
 *         Time: 05:42:13 PM
 */
public class DateUtilsTests extends TestCase {

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
