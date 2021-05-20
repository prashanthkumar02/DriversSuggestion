package com.drivers.suggestion.controller.exceptionHandler.exceptions;

public class NoDataFoundException extends RuntimeException{
    String detailedMessage;
    public NoDataFoundException(String message) {
        super(message);
        this.detailedMessage = message;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }
}
