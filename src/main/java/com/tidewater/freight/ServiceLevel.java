package com.tidewater.freight;

public enum ServiceLevel {
    STANDARD(1.0),
    EXPRESS(1.5);

    private final double priceMultiplier;

    ServiceLevel(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double applyTo(double cost) {
        return cost * priceMultiplier;
    }
}
