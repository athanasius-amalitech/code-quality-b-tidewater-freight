package com.tidewater.freight;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTariffRepository implements TariffRepository {

    private final Map<String, ZoneTariff> rates = new HashMap<>();

    public InMemoryTariffRepository() {
        rates.put("DOMESTIC", new FlatRateTariff(5.0, 0.0));
        rates.put("REGIONAL", new FlatRateTariff(8.0, 10.0));
        rates.put("INTERNATIONAL", new FlatRateTariff(15.0, 25.0));
    }

    @Override
    public ZoneTariff findTariff(String zone) {
        ZoneTariff tariff = rates.get(zone);
        if (tariff == null) {
            throw new IllegalArgumentException("Unknown zone: " + zone);
        }
        return tariff;
    }
}
