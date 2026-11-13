package com.tidewater.freight;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ShippingCalculatorCharacterisationTest {

    private final ShippingCalculator calculator = new ShippingCalculator(new InMemoryTariffRepository());

    @Test
    void chargesBaseRateOnlyForDomestic() {
        double cost = calculator.calculateCost(new Shipment("DOMESTIC", 10, ServiceLevel.STANDARD));

        assertEquals(50, cost, 0.001);
    }

    @Test
    void addsHandlingFeeForRegional() {
        double cost = calculator.calculateCost(new Shipment("REGIONAL", 5, ServiceLevel.STANDARD));

        assertEquals(50, cost, 0.001);
    }

    @Test
    void addsCustomsFeeForInternational() {
        double cost = calculator.calculateCost(new Shipment("INTERNATIONAL", 10, ServiceLevel.STANDARD));

        assertEquals(175, cost, 0.001);
    }

    @Test
    void multipliesTotalForExpress() {
        double cost = calculator.calculateCost(new Shipment("DOMESTIC", 10, ServiceLevel.EXPRESS));

        assertEquals(75, cost, 0.001);
    }

    @Test
    void addsHeavySurchargeOverThirtyKg() {
        double cost = calculator.calculateCost(new Shipment("DOMESTIC", 40, ServiceLevel.STANDARD));

        assertEquals(220, cost, 0.001);
    }

    @Test
    void appliesExpressAfterFeesAndSurcharge() {
        double cost = calculator.calculateCost(new Shipment("INTERNATIONAL", 40, ServiceLevel.EXPRESS));

        assertEquals(967.5, cost, 0.001);
    }

    @Test
    void noHeavySurchargeAtExactlyThirtyKg() {
        double cost = calculator.calculateCost(new Shipment("DOMESTIC", 30, ServiceLevel.STANDARD));

        assertEquals(150, cost, 0.001);
    }

    @Test
    void addsHeavySurchargeJustOverThirtyKg() {
        double cost = calculator.calculateCost(new Shipment("DOMESTIC", 30.1, ServiceLevel.STANDARD));

        assertEquals(170.5, cost, 0.001);
    }

}
