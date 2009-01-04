package org.forzaframework.query;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 15:30:38
 */
public class PageInfo {

    private Integer start;
    private Integer limit;
    private String sort;
    private String dir = "asc";


    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
