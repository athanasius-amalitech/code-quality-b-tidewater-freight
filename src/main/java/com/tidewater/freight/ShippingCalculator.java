package com.tidewater.freight;

import java.util.Objects;

public class ShippingCalculator {

    private final RateRepository rateRepository;

    public ShippingCalculator(RateRepository rateRepository) {
        this.rateRepository = Objects.requireNonNull(rateRepository, "rateRepository");
    }

    public double calculateCost(String zone, double weightKg, String serviceLevel) {
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

            // heavy-item surcharge
            if (weightKg > 30) {
                cost = cost + 20;
            }

            // express costs more
            if (serviceLevel.equals("EXPRESS")) {
                cost = cost * 1.5;
            }

            return cost;
        } catch (Exception e) {
            // could not work out a rate (for example an unknown zone) - return zero
            return 0;
        }
    }
}
