package com.example.employees.exceptions;

public class OnlyOneEmployeeAvailableException extends RuntimeException {
    public OnlyOneEmployeeAvailableException() {
        super();
    }

    public OnlyOneEmployeeAvailableException(String message) {
        super(message);
    }

    public OnlyOneEmployeeAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
