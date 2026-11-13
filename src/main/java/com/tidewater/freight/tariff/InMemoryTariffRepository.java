package com.tidewater.freight.tariff;

import java.util.Map;

public class InMemoryTariffRepository implements TariffRepository {

    private final Map<String, ZoneTariff> tariffs;

    public InMemoryTariffRepository(Map<String, ZoneTariff> tariffs) {
        this.tariffs = Map.copyOf(tariffs);
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
