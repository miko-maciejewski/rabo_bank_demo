package org.miki.rabobankdemo.exceptions;

@SuppressWarnings("serial")
public class WrongParmeterValue extends RuntimeException {
    public WrongParmeterValue(String message) {
        super(message);
    }

    public WrongParmeterValue(String message, Throwable cause) {
        super(message, cause);
    }
}