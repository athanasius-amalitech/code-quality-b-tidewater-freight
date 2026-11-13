package com.tidewater.freight.tariff;

import java.math.BigDecimal;

public record FlatRateTariff(BigDecimal ratePerKg, BigDecimal handlingFee) implements ZoneTariff {

    @Override
    public BigDecimal costFor(BigDecimal weightKg) {
        return ratePerKg.multiply(weightKg).add(handlingFee);
    }
}
