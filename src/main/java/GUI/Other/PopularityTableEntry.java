package GUI.Other;

public class PopularityTableEntry {
    String viewname;
    int dailyvalue, day, viewid;
    boolean isCurrentDay;

    public PopularityTableEntry(String viewname, int dailyvalue, int day, boolean isCurrentDay,int viewid) {
        this.viewname = viewname;
        this.dailyvalue = dailyvalue;
        this.day = day;
        this.viewid = viewid;
        this.isCurrentDay = isCurrentDay;
    }

    public String getViewname() {
        return viewname;
    }

    public int getDailyvalue() {
        return dailyvalue;
    }

    public int getDay() {
        return day;
    }

    public int getViewid() {
        return viewid;
    }

    public boolean isCurrentDay() {
        return isCurrentDay;
    }
}
