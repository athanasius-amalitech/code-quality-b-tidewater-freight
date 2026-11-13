package com.tidewater.freight;

import java.util.HashMap;
import java.util.Map;

import com.tidewater.freight.exceptions.UnknownZoneException;

public class InMemoryTariffRepository implements TariffRepository {

    private final Map<String, ZoneTariff> tariffs = new HashMap<>();

    public InMemoryTariffRepository() {
        tariffs.put("DOMESTIC", new FlatRateTariff(5.0, 0.0));
        tariffs.put("REGIONAL", new FlatRateTariff(8.0, 10.0));
        tariffs.put("INTERNATIONAL", new FlatRateTariff(15.0, 25.0));
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
