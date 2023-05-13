package com.example.employees.exceptions;

public class EmptyCsvFileException extends RuntimeException {
    public EmptyCsvFileException() {
        super();
    }

    public EmptyCsvFileException(String message) {
        super(message);
    }

    public EmptyCsvFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
