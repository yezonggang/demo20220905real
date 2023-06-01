package com.quick.start.demo.config.datasource;

/**
 * @author y25958
 */
public class StatusChangedEvent {
    private String status;

    public StatusChangedEvent(String status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
