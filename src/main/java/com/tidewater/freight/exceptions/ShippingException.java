package com.tidewater.freight.exceptions;

public abstract class ShippingException extends RuntimeException {

    public ShippingException(String message) {
        super(message);
    }
}
