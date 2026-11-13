package com.tidewater.freight.shipment;

import com.tidewater.freight.ShippingException;

public class InvalidShipmentException extends ShippingException {

    public InvalidShipmentException(String message) {
        super("Invalid shipment: " + message);
    }
}
