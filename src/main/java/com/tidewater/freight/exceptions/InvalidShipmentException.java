package com.tidewater.freight.exceptions;

public class InvalidShipmentException extends ShippingException {

    public InvalidShipmentException(String message) {
        super("Invalid shipment: " + message);
    }
}
