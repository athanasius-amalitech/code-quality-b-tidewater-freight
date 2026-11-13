package com.tidewater.freight.exceptions;

public class UnknownZoneException extends ShippingException {

    private final String zone;

    public UnknownZoneException(String zone) {
        super("Unknown zone: " + zone);
        this.zone = zone;
    }

    public String getZone() {
        return zone;
    }
}
