package org.forzaframework.util;

import java.util.Date;

/**
 * @author cesarreyes
 *         Date: 10-oct-2008
 *         Time: 11:09:07
 */
public class DateRange {

    private Date startDate;
    private Date endDate;

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getDays(){
        throw new RuntimeException("Not Implemented");
    }
}
