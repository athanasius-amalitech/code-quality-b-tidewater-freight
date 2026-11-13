package com.tidewater.freight.shipment;

import java.math.BigDecimal;

public record Shipment(String zone, BigDecimal weightKg, ServiceLevel serviceLevel) {
    public Shipment {
        if (zone == null || zone.isBlank()) {
            throw new InvalidShipmentException("Zone cannot be null or blank");
        }
        if (serviceLevel == null) {
            throw new InvalidShipmentException("Service level cannot be null");
        }
        if (weightKg == null || weightKg.signum() <= 0) {
            throw new InvalidShipmentException("Weight must be positive, was " + weightKg);
        }
    }
}
