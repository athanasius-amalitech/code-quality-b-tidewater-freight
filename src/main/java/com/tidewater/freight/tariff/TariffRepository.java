package com.tidewater.freight.tariff;

public interface TariffRepository {
    ZoneTariff findTariff(String zone);
}
