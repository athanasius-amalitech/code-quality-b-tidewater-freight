package com.tidewater.freight;

import java.math.BigDecimal;

public record FlatRateTariff(BigDecimal ratePerKg, BigDecimal handlingFee) implements ZoneTariff {

    @Override
    public BigDecimal costFor(double weightKg) {
        return ratePerKg.multiply(BigDecimal.valueOf(weightKg)).add(handlingFee);
    }
}
