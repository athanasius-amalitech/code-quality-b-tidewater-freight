package com.tidewater.freight;

import java.util.Objects;

public class ShippingCalculator {

    private final RateRepository rateRepository;
    private static final double HEAVY_ITEM_THRESHOLD_KG = 30;
    private static final double HEAVY_ITEM_SURCHARGE = 20;

    public ShippingCalculator(RateRepository rateRepository) {
        this.rateRepository = Objects.requireNonNull(rateRepository, "rateRepository");
    }

    public double calculateCost(String zone, double weightKg, ServiceLevel serviceLevel) {
        try {
            double rate = rateRepository.findRate(zone);
            double cost = rate * weightKg;

            // zone-specific fees
            if (zone.equals("DOMESTIC")) {
                // no extra fee
            } else if (zone.equals("REGIONAL")) {
                cost = cost + 10; // handling fee
            } else if (zone.equals("INTERNATIONAL")) {
                cost = cost + 25; // customs handling
            }

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
