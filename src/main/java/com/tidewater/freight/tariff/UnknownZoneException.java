package com.tidewater.freight.tariff;

import com.tidewater.freight.ShippingException;

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
