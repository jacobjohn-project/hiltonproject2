package com.futurearts.hiltonnewproj.modelclasses;

public class BatchContraolDetails {
    String job_number,part_number,batch_number;
    int quantity;
    String image_url;

    public BatchContraolDetails() {
    }

    public BatchContraolDetails(String job_number, String part_number, String batch_number, int quantity, String image_url) {
        this.job_number = job_number;
        this.part_number = part_number;
        this.batch_number = batch_number;
        this.quantity = quantity;
        this.image_url = image_url;
    }

    public BatchContraolDetails(String job_number, String part_number, String batch_number, int quantity) {
        this.job_number = job_number;
        this.part_number = part_number;
        this.batch_number = batch_number;
        this.quantity = quantity;
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
}
