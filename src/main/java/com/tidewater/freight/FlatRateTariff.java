package com.tidewater.freight;

public record FlatRateTariff(double ratePerKg, double handlingFee) implements ZoneTariff {

    @Override
    public double costFor(double weightKg) {
        return (weightKg * ratePerKg) + handlingFee;
    }
}
