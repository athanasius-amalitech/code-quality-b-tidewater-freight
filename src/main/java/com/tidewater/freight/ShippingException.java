package com.tidewater.freight;

public abstract class ShippingException extends RuntimeException {

    protected ShippingException(String message) {
        super(message);
    }

    protected ShippingException(String message, Throwable cause) {
        super(message, cause);
    }
}
