package com.futurearts.hiltonnewproj.modelclasses;

public class MaterialIssueDetails {
    public String job_Num;
    public int part_Num,qty_shortage;
    public String who;
    public String saved_date;

    public MaterialIssueDetails(String job_Num, int part_Num, int qty_shortage, String who, String saved_date) {
        this.job_Num = job_Num;
        this.part_Num = part_Num;
        this.qty_shortage = qty_shortage;
        this.who = who;
        this.saved_date = saved_date;
    }

    public MaterialIssueDetails() {
    }

    public String getJob_Num() {
        return job_Num;
    }

    public void setJob_Num(String job_Num) {
        this.job_Num = job_Num;
    }

    public int getPart_Num() {
        return part_Num;
    }

    public void setPart_Num(int part_Num) {
        this.part_Num = part_Num;
    }

    public int getQty_shortage() {
        return qty_shortage;
    }

    public void setQty_shortage(int qty_shortage) {
        this.qty_shortage = qty_shortage;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getSaved_date() {
        return saved_date;
    }

    public void setSaved_date(String saved_date) {
        this.saved_date = saved_date;
    }
}
