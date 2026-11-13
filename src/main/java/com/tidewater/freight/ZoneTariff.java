package com.tidewater.freight;

import java.math.BigDecimal;

public interface ZoneTariff {
    BigDecimal costFor(double weightKg);
}
