package com.futurearts.hiltonnewproj.modelclasses;

public class BatchContraolDetails {
    String job_number,part_number,batch_number;
    int quantity;
    String image_url;
    String work_center,date_time,added_by;

    public BatchContraolDetails() {
    }

    public BatchContraolDetails(String job_number, String part_number, String batch_number, int quantity, String image_url) {
        this.job_number = job_number;
        this.part_number = part_number;
        this.batch_number = batch_number;
        this.quantity = quantity;
        this.image_url = image_url;
    }

    public BatchContraolDetails(String job_number, String part_number, String batch_number, int quantity, String work_center,String date_time,String added_by) {
        this.job_number = job_number;
        this.part_number = part_number;
        this.batch_number = batch_number;
        this.quantity = quantity;
        this.work_center=work_center;
        this.date_time=date_time;
        this.added_by=added_by;

    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    public String getBatch_number() {
        return batch_number;
    }

    public void setBatch_number(String batch_number) {
        this.batch_number = batch_number;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setWork_center(String work_center) {
        this.work_center = work_center;
    }

    public String getWork_center() {
        return work_center;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }
}
