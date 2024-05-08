package com.example.orders.utils.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(){

    }
    public OrderNotFoundException(String message){super(message);}
    public OrderNotFoundException(Throwable cause){super(cause);}
    public OrderNotFoundException(String message, Throwable cause){super(message,cause);}
}
