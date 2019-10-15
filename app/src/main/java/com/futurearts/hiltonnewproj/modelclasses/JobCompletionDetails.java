package com.futurearts.hiltonnewproj.modelclasses;

public class JobCompletionDetails {
    public String job_Num;
    public int qty_completed;
    public String who;
    public String job_image;
    public String saved_date;

    public JobCompletionDetails(String job_Num, int qty_completed, String who,String saved_date) {
        this.job_Num = job_Num;
        this.qty_completed = qty_completed;
        this.who = who;
        this.saved_date=saved_date;
    }

    public JobCompletionDetails() {
    }

    public String getJob_Num() {
        return job_Num;
    }

    public void setJob_Num(String job_Num) {
        this.job_Num = job_Num;
    }

    public int getQty_completed() {
        return qty_completed;
    }

    public void setQty_completed(int qty_completed) {
        this.qty_completed = qty_completed;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getJob_image() {
        return job_image;
    }

    public void setJob_image(String job_image) {
        this.job_image = job_image;
    }
}
