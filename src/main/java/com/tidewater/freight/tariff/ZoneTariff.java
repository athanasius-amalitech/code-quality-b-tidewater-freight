package com.tidewater.freight.tariff;

import java.math.BigDecimal;

public interface ZoneTariff {
    BigDecimal costFor(BigDecimal weightKg);
}
