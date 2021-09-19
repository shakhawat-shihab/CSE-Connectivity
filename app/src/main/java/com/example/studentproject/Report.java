package com.example.studentproject;

public class Report {
    String id, batch, subject, description,reportBy_id,reportBy_name,reportBy_mobile;
    String key;

    public Report(String subject, String description, String reportBy_id, String reportBy_name, String reportBy_mobile) {
        this.subject = subject;
        this.description = description;
        this.reportBy_id = reportBy_id;
        this.reportBy_name = reportBy_name;
        this.reportBy_mobile = reportBy_mobile;
    }

    public Report() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportBy_id() {
        return reportBy_id;
    }

    public void setReportBy_id(String reportBy_id) {
        this.reportBy_id = reportBy_id;
    }

    public String getReportBy_name() {
        return reportBy_name;
    }

    public void setReportBy_name(String reportBy_name) {
        this.reportBy_name = reportBy_name;
    }

    public String getReportBy_mobile() {
        return reportBy_mobile;
    }

    public void setReportBy_mobile(String reportBy_mobile) {
        this.reportBy_mobile = reportBy_mobile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
