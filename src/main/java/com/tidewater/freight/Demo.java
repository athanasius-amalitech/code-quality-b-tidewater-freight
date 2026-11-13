package com.tidewater.freight;

import com.tidewater.freight.exceptions.ShippingException;

public class Demo {
        public static void main(String[] args) {

                TariffRepository tariffRepository = new InMemoryTariffRepository();
                ShippingCalculator calculator = new ShippingCalculator(tariffRepository);

                System.out.println("Result DOMESTIC 10kg STANDARD       -> "
                                + calculator.calculateCost(new Shipment("DOMESTIC", 10, ServiceLevel.STANDARD)));
                System.out.println("Result INTERNATIONAL 40kg EXPRESS   -> "
                                + calculator.calculateCost(new Shipment("INTERNATIONAL", 40, ServiceLevel.EXPRESS)));

                try {
                        System.out.println("Result MOON 10kg STANDARD -> "
                                        + calculator.calculateCost(new Shipment("MOON", 10, ServiceLevel.STANDARD)));
                } catch (ShippingException e) {
                        System.out.println("Could not price shipment: " + e.getMessage());
                }
        }
}
