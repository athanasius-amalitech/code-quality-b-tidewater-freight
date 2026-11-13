package com.tidewater.freight;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ShippingCalculator {

    private static final double HEAVY_ITEM_THRESHOLD_KG = 30;
    private static final BigDecimal HEAVY_ITEM_SURCHARGE = new BigDecimal("20");
    private static final int PRICE_DECIMAL_PLACES = 2;

    private final TariffRepository tariffRepository;

    public ShippingCalculator(TariffRepository tariffRepository) {
        this.tariffRepository = Objects.requireNonNull(tariffRepository, "tariffRepository");
    }

    public BigDecimal calculateCost(Shipment shipment) {
        ZoneTariff tariff = tariffRepository.findTariff(shipment.zone());
        BigDecimal cost = BigDecimal.valueOf(tariff.costFor(shipment.weightKg()));

        if (shipment.weightKg() > HEAVY_ITEM_THRESHOLD_KG) {
            cost = cost.add(HEAVY_ITEM_SURCHARGE);
        }

        cost = shipment.serviceLevel().applyTo(cost);

        return cost;
    }
}
