package com.tidewater.freight.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import com.tidewater.freight.shipment.Shipment;
import com.tidewater.freight.tariff.TariffRepository;
import com.tidewater.freight.tariff.ZoneTariff;

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
        BigDecimal cost = tariff.costFor(shipment.weightKg());

        if (shipment.weightKg() > HEAVY_ITEM_THRESHOLD_KG) {
            cost = cost.add(HEAVY_ITEM_SURCHARGE);
        }

        return shipment.serviceLevel().applyTo(cost)
                .setScale(PRICE_DECIMAL_PLACES, RoundingMode.HALF_UP);
    }
}
