package com.tidewater.freight.shipment;

import java.math.BigDecimal;

public enum ServiceLevel {
    STANDARD(new BigDecimal("1.0")),
    EXPRESS(new BigDecimal("1.5"));

    private final BigDecimal priceMultiplier;

    ServiceLevel(BigDecimal priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public BigDecimal applyTo(BigDecimal cost) {
        return cost.multiply(priceMultiplier);
    }
}
