package com.futurearts.hiltonnewproj.modelclasses;

public class ReportIssueDetails {
    private String job_number,part_number,work_center,operator,added_by,date_time;
    private int quantity;
    private String issue;
    private String notes;

    public ReportIssueDetails() {
    }

    public ReportIssueDetails(String job_number, String part_number, String work_center, String operator, int quantity,String added_by,String date_time, String issue, String notes) {
        this.job_number = job_number;
        this.part_number = part_number;
        this.work_center = work_center;
        this.operator = operator;
        this.quantity = quantity;
        this.issue = issue;
        this.notes = notes;
        this.added_by=added_by;
        this.date_time=date_time;
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

    public String getWork_center() {
        return work_center;
    }

    public void setWork_center(String work_center) {
        this.work_center = work_center;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
