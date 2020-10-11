package com.zak.hoaxify.Error;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ApiError {
    private long timestamp = new Date().getTime();
    private int status;
    private String message;
    private String url;
    private Map<String , String > validationErrors;

    public ApiError() {
    }

    public ApiError(int status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }
}
