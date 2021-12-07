package com.example.myschedule.db;

import org.litepal.crud.LitePalSupport;
import java.util.Date;

public class Plan extends LitePalSupport {

    int id;

    String year;

    String month;

    String day;

    boolean status;

    Date createTime;

    String writePlan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getWritePlan() {
        return writePlan;
    }

    public void setWritePlan(String writePlan) {
        this.writePlan = writePlan;
    }
}
