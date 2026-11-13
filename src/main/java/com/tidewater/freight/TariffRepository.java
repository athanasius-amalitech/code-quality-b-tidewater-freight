package com.tidewater.freight;

public interface TariffRepository {
    ZoneTariff findTariff(String zone);
}
