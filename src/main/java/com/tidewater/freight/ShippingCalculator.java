package com.tidewater.freight;

import java.util.Objects;

public class ShippingCalculator {

    private static final double HEAVY_ITEM_THRESHOLD_KG = 30;
    private static final double HEAVY_ITEM_SURCHARGE = 20;
    private final TariffRepository tariffRepository;

    public ShippingCalculator(TariffRepository tariffRepository) {
        this.tariffRepository = Objects.requireNonNull(tariffRepository, "tariffRepository");
    }

    public double calculateCost(String zone, double weightKg, ServiceLevel serviceLevel) {
        try {

            ZoneTariff tariff = tariffRepository.findTariff(zone);
            double cost = tariff.costFor(weightKg);

            if (weightKg > HEAVY_ITEM_THRESHOLD_KG) {
                cost = cost + HEAVY_ITEM_SURCHARGE;
            }

            cost = serviceLevel.applyTo(cost);

            return cost;
        } catch (Exception e) {
            // could not work out a rate (for example an unknown zone) - return zero
            return 0;
        }
    }
}
