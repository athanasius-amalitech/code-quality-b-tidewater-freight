package com.tidewater.freight;

public abstract class ShippingException extends RuntimeException {

    public ShippingException(String message) {
        super(message);
    }
}
