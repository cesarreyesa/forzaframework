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

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormatSymbols;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 12:39:21
 */
/**
 * Date Utility Class
 * This is used to convert Strings to Dates and Timestamps
 * <p/>
 * <p/>
 * <a href="DateUtils.java.html"><i>View Source</i></a>
 * </p>
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
    //~ Static fields/initializers =============================================

    private static Log log = LogFactory.getLog(DateUtils.class);
    private static String defaultDatePattern = null;
    private static String timePattern = "HH:mm";
    private static final String APPLICATION_RESOURCES = "ApplicationResources";

    //~ Methods ================================================================

    /**
     * Return default datePattern (MM/dd/yyyy)
     *
     * @return a string representing the date pattern on the UI
     */
    public static synchronized String getDatePattern() {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            defaultDatePattern = ResourceBundle.getBundle(APPLICATION_RESOURCES, locale)
                    .getString("date.format");
        } catch (MissingResourceException mse) {
            defaultDatePattern = "yyyy/MM/dd";
        }

        return defaultDatePattern;
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param date date from database as a string
     * @return formatted string for the ui
     */
    public static String getString(Date date) {
        SimpleDateFormat df;
        String returnValue = "";

        if (date != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(date);
        }

        return (returnValue);
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param date date from database as a string
     * @return formatted string for the ui
     */
    public static String getString(Date date, String format) {
        SimpleDateFormat df;
        String returnValue = "";

        if (date != null) {
            df = new SimpleDateFormat(format != null ? format : getDatePattern());
            returnValue = df.format(date);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param mask    the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @throws java.text.ParseException
     * @see java.text.SimpleDateFormat
     */
    public static Date getDate(String mask, String strDate)
            throws ParseException {
        SimpleDateFormat df;
        Date date;
        df = new SimpleDateFormat(mask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '"
                    + mask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * @throws ParseException
     */
    public static Date getDate(String strDate) {
        Date date;
        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + getDatePattern());
            }

            date = getDate(getDatePattern(), strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate + "' to a date, throwing exception");

            throw new RuntimeException(new ParseException(pe.getMessage(), pe.getErrorOffset()));
        }

        return date;
    }


    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param mask the date pattern the string is in
     * @param date a date object
     * @return a formatted string representation of the date
     * @see java.text.SimpleDateFormat
     */
    public static String getDateTime(String mask, Date date) {
        String returnValue = "";

        if (date == null) {
            log.error("date is null!");
        } else {
            SimpleDateFormat df = new SimpleDateFormat(mask);
            returnValue = df.format(date);
        }

        return (returnValue);
    }

    public static int getDaysDifference(Date startDate, Date endDate) {
        Calendar arrival = Calendar.getInstance();
        Calendar departure = Calendar.getInstance();

        arrival.setTime(startDate);
        Long millisecondsArrivalDate = startDate.getTime() + arrival.get(Calendar.ZONE_OFFSET) + arrival.get(Calendar.DST_OFFSET);
        int hoursArrival = (int) (millisecondsArrivalDate / 3600000);
        int daysArrival = hoursArrival / 24;

        departure.setTime(endDate);
        Long millisecondsDepartureDate = endDate.getTime() + departure.get(Calendar.ZONE_OFFSET) + departure.get(Calendar.DST_OFFSET);
        int hoursDeparture = (int) (millisecondsDepartureDate / 3600000);
        int daysDeparture = hoursDeparture / 24;
        int days = daysDeparture - daysArrival;

        return days;
    }

    public static Map<String, Integer> calculateDays(int days) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int months = 0;
        int weeks = 0;
        if (days >= 30) {
            months = days / 30;
            int modMonths = days % 30;
            if (modMonths >= 7) {
                weeks = modMonths / 7;
                days = modMonths % 7;
            } else {
                days = modMonths;
            }
        } else if (days >= 7) {
            weeks = days / 7;
            days = days % 7;
        }
        map.put("months", months);
        map.put("weeks", weeks);
        map.put("days", days);

        return map;
    }

    public static Calendar createCalendar(int year, int month) {
        return createCalendar(year, month, 1, 0, 0, 0, 0);
    }

    public static Calendar createCalendar(int year, int month, int day) {
        return createCalendar(year, month, day, 0, 0, 0, 0);
    }

    public static Calendar createCalendar(int year, int month, int day, int hour, int minute) {
        return createCalendar(year, month, day, hour, minute, 0, 0);
    }

    public static Calendar createCalendar(int year, int month, int day, int hour, int minute, int second) {
        return createCalendar(year, month, day, hour, minute, second, 0);
    }

    public static Calendar createCalendar(int year, int month, int day, int hour, int minute, int second, int milisecond) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, milisecond);
        return cal;
    }

    public static Calendar createCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    public static Date getFinalDateOfMonth(Date date) {
        date = truncate(date, Calendar.MONTH);
        date = addMonths(date, 1);
        return addDays(date, -1);
    }

    public static Boolean isSameDayOfWeek(Date dateOne, Date dateTwo) {
        Calendar c1 = createCalendar(dateOne);
        Calendar c2 = createCalendar(dateTwo);
        return c1.get(Calendar.DAY_OF_WEEK) == c2.get(Calendar.DAY_OF_WEEK);
    }

    public static Boolean isSameDayOfWeek(Integer dayOfWeek, Date date) {
        Calendar c1 = createCalendar(date);
        return dayOfWeek == c1.get(Calendar.DAY_OF_WEEK);
    }

    public static Boolean isSameDayOfMonth(Date dateOne, Date dateTwo) {
        Boolean isSame = false;
        Calendar c1 = DateUtils.createCalendar(dateOne);
        Calendar c2 = DateUtils.createCalendar(dateTwo);
        if (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))
            isSame = true;

        return isSame;
    }

    public static Boolean isSameDayOfMonth(Date date, Integer dayOfMonth) {
        Boolean isSame = false;
        Calendar c1 = DateUtils.createCalendar(date);
        if (c1.get(Calendar.DAY_OF_MONTH) == dayOfMonth)
            isSame = true;

        return isSame;
    }

    public static Integer compareDate(Date dateOne, Date dateTwo) {
        Integer result;
        if (DateUtils.isSameDay(dateOne, dateTwo))
            result = 0;
        else if (dateOne.compareTo(dateTwo) > 0)
            result = 1;
        else
            result = -1;

        return result;
    }

    public static Integer getDaysOfYear(Integer year) {
        Date starDate = DateUtils.createCalendar(year, Calendar.JANUARY, 1).getTime();
        Date endDate = DateUtils.createCalendar(year, Calendar.DECEMBER, 31).getTime();
        return DateUtils.getDaysDifference(starDate, endDate) + 1;
    }

    public static Integer getDayOfMonth(Date date) {
        return DateUtils.createCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getDaysOfMonth(Integer year, Integer month) {
        return createCalendar(year, month).getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Integer getDayOfWeek(Date date) {
        return DateUtils.createCalendar(date).get(Calendar.DAY_OF_WEEK);
    }

    public static String getDayOfWeek(Integer day) {
        Assert.isTrue(Calendar.SUNDAY >= 0 && day <= Calendar.SATURDAY, "Error. El parametro dia no corresponde a un dia de la semana");
        String[] days = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        //Restamos 1 porque el array empieza con 0;
        day--;

        return days[day];
    }

    public static String getMonth(Integer month){
        Locale locale = LocaleContextHolder.getLocale();
        DateFormatSymbols dfs = new DateFormatSymbols(locale);

        return dfs.getMonths()[month];
    }

    /**
     * Regresa verdadero si la primer fecha esta entre las otras 2, ignorando los anios, es decir, 25 de febrero, esta entre 1 enero y 29 de febrero
     * @param dateToCompare
     * @param firstDate
     * @param lastDate
     * @return
     */
    public static Boolean isBetweenDatesIgnoreYear(Date dateToCompare, Date firstDate, Date lastDate){
        Calendar date = DateUtils.createCalendar(dateToCompare);
        if(date.get(Calendar.YEAR) < DateUtils.createCalendar(firstDate).get(Calendar.YEAR)){
            Integer day = date.get(Calendar.DAY_OF_YEAR);
            Integer firstDay = DateUtils.createCalendar(firstDate).get(Calendar.DAY_OF_YEAR);
            Integer lastDay = DateUtils.createCalendar(lastDate).get(Calendar.DAY_OF_YEAR);
            if(day >= firstDay && day <= lastDay){
                return true;
            }
        }
        return false;
    }

    public static Date fromSmartDate(String smartDate){
    	return fromSmartDate(smartDate, new Date());
    }

    public static Date fromSmartDate(String smartDate, Date date){
    	Integer negative = 1;
    	if(smartDate.startsWith("-")){
    		negative = -1;
    		smartDate = smartDate.substring(1);
    	}
    	String[] tokens = smartDate.split(" ");
    	for(String token : tokens){
    		Integer amount = Integer.valueOf(token.substring(0, token.length() - 1));
    		amount *= negative;
    		if(token.endsWith("w")){
    			date = addWeeks(date, amount);
    		}else if(token.endsWith("d")){
    			date = addDays(date, amount);
    		}else if(token.endsWith("h")){
    			date = addHours(date, amount);
    		}else if(token.endsWith("m")){
    			date = addMinutes(date, amount);
    		}else if(token.endsWith("s")){
    			date = addSeconds(date, amount);
    		}
    	}
    	return date;
    }

    public static String toCuteDate(Date date) {
        Date now = new Date();
        double difference = Math.ceil(now.getTime() - date.getTime());
        // pasado
        if (difference > 0) {
            double days = difference / 86400000; // numero de milisegundos en un dia = 86400000
            if (days > 365) {
                return String.format("Hace %1$s años", Math.round(days / 365));
            }
            if (days > 30) {
                return String.format("Hace %1$s meses", Math.round(days / 30.42));
            }
            if (days > 1) {
                return String.format("Hace %1$s dias", Math.round(days));
            }
            double horas = Math.round(difference / 60/ 60 / 1000);
            if (horas > 1) {
                return String.format("Hace %1$s horas", Math.round(horas));
            }
            double minutes = Math.round(difference / 60 / 1000);
            if (minutes > 10) {
                return String.format("Hace %1$s minutos", Math.round(minutes));
            }
            return String.format("Hace unos pocos minutos");
        } else {
            // futuro
            return date.toString();
        }
    }

    public static boolean isInPast(Date date){
        return isInPast(new Date(), date);
    }

    public static boolean isInPast(Date referenceDate, Date evalDate){
        return evalDate.compareTo(referenceDate) < 0;
    }

    public static Integer getMonthsDifference(Date startDate, Date endDate){
        Calendar startDateCal = createCalendar(startDate);
        Calendar endDateCal = createCalendar(endDate);
        // nos aseguramos de que la segunda fecha este en el pasado.
        Assert.isTrue(DateUtils.isInPast(startDate, endDate), "La fecha [" + endDate + "] debe estar antes de [" + endDate + "]");
        // si es el mismo anio entonces solo resta los meses
        startDateCal.clear(Calendar.MILLISECOND);
        startDateCal.clear(Calendar.SECOND);
        startDateCal.clear(Calendar.MINUTE);
        startDateCal.clear(Calendar.HOUR_OF_DAY);
        endDateCal.clear(Calendar.MILLISECOND);
        endDateCal.clear(Calendar.SECOND);
        endDateCal.clear(Calendar.MINUTE);
        endDateCal.clear(Calendar.HOUR_OF_DAY);
        int elapsed = 0;
        Date d1 = startDateCal.getTime();
        Date d2 = endDateCal.getTime();
        while (isInPast(d1, d2)) {
            d2 = addMonths(d2, 1);
            elapsed++;

            Calendar cal1 = createCalendar(d1);
            Calendar cal2 = createCalendar(d2);
            if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)){
                if(cal1.get(Calendar.DAY_OF_MONTH) > cal2.get(Calendar.DAY_OF_MONTH)){
                    elapsed--;
                }
            }
        }
        return elapsed;
    }

}
