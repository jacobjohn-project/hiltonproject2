package com.futurearts.hiltonnewproj.modelclasses;

public class BatchContraolDetails {
    String job_number,part_number,batch_number;
    String po_number;
    int quantity;
    String image_url;
    String work_center,date_time,added_by;
    String operator;
    String startOrFinish;
    String type;
    String notes;
    String job_part_work_operator;
    String issue;

    public BatchContraolDetails() {
    }

    public BatchContraolDetails(String job_number, String part_number, String batch_number, int quantity, String image_url) {
        this.job_number = job_number;
        this.part_number = part_number;
        this.batch_number = batch_number;
        this.quantity = quantity;
        this.image_url = image_url;
    }

    public BatchContraolDetails(String job_number, String part_number, String batch_number, int quantity, String work_center,String date_time,String added_by,String operator,String po_number,String type,String startOrFinish,String notes,String job_part_work_operator) {
        this.job_number = job_number;
        this.part_number = part_number;
        this.batch_number = batch_number;
        this.quantity = quantity;
        this.work_center=work_center;
        this.date_time=date_time;
        this.added_by=added_by;
        this.operator=operator;
        this.po_number = po_number;
        this.type=type;
        this.startOrFinish=startOrFinish;
        this.notes=notes;
        this.job_part_work_operator=job_part_work_operator;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public String getStartOrFinish() {
        return startOrFinish;
    }

    public void setStartOrFinish(String startOrFinish) {
        this.startOrFinish = startOrFinish;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getJob_part_work_operator() {
        return job_part_work_operator;
    }

    public void setJob_part_work_operator(String job_part_work_operator) {
        this.job_part_work_operator = job_part_work_operator;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public void appendNote(String newNote){
        this.notes=this.notes+"\n"+newNote;
    }
}
