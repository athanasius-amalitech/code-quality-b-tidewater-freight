package com.tidewater.freight;

import java.util.Objects;

public class ShippingCalculator {

    private static final double HEAVY_ITEM_THRESHOLD_KG = 30;
    private static final double HEAVY_ITEM_SURCHARGE = 20;
    private final TariffRepository tariffRepository;

    public ShippingCalculator(TariffRepository tariffRepository) {
        this.tariffRepository = Objects.requireNonNull(tariffRepository, "tariffRepository");
    }

    public double calculateCost(Shipment shipment) {
        ZoneTariff tariff = tariffRepository.findTariff(shipment.zone());
        double cost = tariff.costFor(shipment.weightKg());

        if (shipment.weightKg() > HEAVY_ITEM_THRESHOLD_KG) {
            cost = cost + HEAVY_ITEM_SURCHARGE;
        }

        cost = shipment.serviceLevel().applyTo(cost);

        return cost;
    }
}
