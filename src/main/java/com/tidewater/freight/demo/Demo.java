package com.tidewater.freight.demo;

import com.tidewater.freight.ShippingException;
import com.tidewater.freight.pricing.ShippingCalculator;
import com.tidewater.freight.shipment.ServiceLevel;
import com.tidewater.freight.shipment.Shipment;
import com.tidewater.freight.tariff.InMemoryTariffRepository;
import com.tidewater.freight.tariff.TariffRepository;

public class Demo {

    private static final double PARCEL_WEIGHT_KG = 10;
    private static final double HEAVY_PARCEL_WEIGHT_KG = 40;

    public static void main(String[] args) {
        TariffRepository tariffRepository = new InMemoryTariffRepository();
        ShippingCalculator calculator = new ShippingCalculator(tariffRepository);

        System.out.println("Result DOMESTIC 10kg STANDARD       -> "
                + calculator.calculateCost(new Shipment("DOMESTIC", PARCEL_WEIGHT_KG,
                        ServiceLevel.STANDARD)));
        System.out.println("Result INTERNATIONAL 40kg EXPRESS   -> "
                + calculator.calculateCost(new Shipment("INTERNATIONAL", HEAVY_PARCEL_WEIGHT_KG,
                        ServiceLevel.EXPRESS)));

        try {
            System.out.println("Result MOON 10kg STANDARD -> "
                    + calculator.calculateCost(new Shipment("MOON", PARCEL_WEIGHT_KG,
                            ServiceLevel.STANDARD)));
        } catch (ShippingException e) {
            System.out.println("Could not price shipment: " + e.getMessage());
        }
    }
}
