package com.tidewater.freight;

import com.tidewater.freight.exceptions.InvalidShipmentException;

public record Shipment(String zone, double weightKg, ServiceLevel serviceLevel) {
    public Shipment {
        if (zone == null || zone.isBlank()) {
            throw new InvalidShipmentException("Zone cannot be null or blank");
        }
        if (serviceLevel == null) {
            throw new InvalidShipmentException("Service level cannot be null");
        }
        if (!Double.isFinite(weightKg) || weightKg <= 0) {
            throw new InvalidShipmentException(
                    "Weight must be positive and finite, was " + weightKg);
        }
    }
}
