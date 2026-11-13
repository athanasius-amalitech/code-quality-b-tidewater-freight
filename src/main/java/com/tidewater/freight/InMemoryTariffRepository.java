package com.tidewater.freight;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.tidewater.freight.exceptions.UnknownZoneException;

public class InMemoryTariffRepository implements TariffRepository {

    private final Map<String, ZoneTariff> tariffs = new HashMap<>();

    public InMemoryTariffRepository() {
        tariffs.put("DOMESTIC", new FlatRateTariff(new BigDecimal("5"), BigDecimal.ZERO));
        tariffs.put("REGIONAL", new FlatRateTariff(new BigDecimal("8"), new BigDecimal("10")));
        tariffs.put("INTERNATIONAL",
                new FlatRateTariff(new BigDecimal("15"), new BigDecimal("25")));
    }

    @Override
    public ZoneTariff findTariff(String zone) {
        ZoneTariff tariff = tariffs.get(zone);
        if (tariff == null) {
            throw new UnknownZoneException(zone);
        }
        return tariff;
    }
}
