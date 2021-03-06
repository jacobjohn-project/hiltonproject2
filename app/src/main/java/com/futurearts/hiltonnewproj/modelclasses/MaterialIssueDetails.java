package com.futurearts.hiltonnewproj.modelclasses;

public class MaterialIssueDetails {
    public String job_Num;
    public int qty_shortage;
    public String part_Num;
    public String who;
    public String saved_date;
    public String requiredLocation;
    public boolean urgent;
    public String materialJobImage;
    public String emailed_To;
    public String doneBy;
    public String packOrEach;
    public String whereTo,whereFrom;
    public String notes;

    public MaterialIssueDetails(String job_Num, String part_Num, int qty_shortage, String who, String saved_date,String requiredLocation,boolean urgent,String packOrEach,String editWhereFrom,String editWhereTo) {
        this.job_Num = job_Num;
        this.part_Num = part_Num;
        this.qty_shortage = qty_shortage;
        this.who = who;
        this.saved_date = saved_date;
        this.requiredLocation=requiredLocation;
        this.urgent=urgent;
        this.packOrEach=packOrEach;
        this.whereFrom=editWhereFrom;
        this.whereTo=editWhereTo;
    }

    public MaterialIssueDetails() {
    }

    public String getJob_Num() {
        return job_Num;
    }

    public void setJob_Num(String job_Num) {
        this.job_Num = job_Num;
    }

    public String getPart_Num() {
        return part_Num;
    }

    public void setPart_Num(String part_Num) {
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

    public String getRequiredLocation() {
        return requiredLocation;
    }

    public void setRequiredLocation(String requiredLocation) {
        this.requiredLocation = requiredLocation;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getMaterialJobImage() {
        return materialJobImage;
    }

    public void setMaterialJobImage(String materialJobImage) {
        this.materialJobImage = materialJobImage;
    }

    public String getEmailed_To() {
        return emailed_To;
    }

    public void setEmailed_To(String emailed_To) {
        this.emailed_To = emailed_To;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
    }

    public String getPackOrEach() {
        return packOrEach;
    }

    public void setPackOrEach(String packOrEach) {
        this.packOrEach = packOrEach;
    }

    public String getWhereTo() {
        return whereTo;
    }

    public void setWhereTo(String editWhereTo) {
        this.whereTo = editWhereTo;
    }

    public String getWhereFrom() {
        return whereFrom;
    }

    public void setWhereFrom(String editWhereFrom) {
        this.whereFrom = editWhereFrom;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
