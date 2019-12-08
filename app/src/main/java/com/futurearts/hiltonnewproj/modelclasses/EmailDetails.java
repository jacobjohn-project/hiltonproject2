package com.futurearts.hiltonnewproj.modelclasses;

public class EmailDetails {
    String email_id,part_number;

    public EmailDetails() {
    }

    public EmailDetails(String email_id, String part_number) {
        this.email_id = email_id;
        this.part_number = part_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }
}
