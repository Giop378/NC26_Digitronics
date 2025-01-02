package it.unisa.nc26.digitronics.utils;

import jakarta.servlet.ServletException;

public class MyServletException extends ServletException {

    public MyServletException() {
        super();
    }

    public MyServletException(String message) {
        super(message);
    }
}
