package com.tidewater.freight;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ShippingCalculatorCharacterisationTest {

    private final ShippingCalculator calculator = new ShippingCalculator(new InMemoryRateRepository());

    @Test
    void chargesBaseRateOnlyForDomestic() {
        double cost = calculator.calculateCost("DOMESTIC", 10, "STANDARD");

        assertEquals(50, cost, 0.001);
    }

    @Test
    void addsHandlingFeeForRegional() {
        double cost = calculator.calculateCost("REGIONAL", 5, "STANDARD");

        assertEquals(50, cost, 0.001);
    }

    @Test
    void addsCustomsFeeForInternational() {
        double cost = calculator.calculateCost("INTERNATIONAL", 10, "STANDARD");

        assertEquals(175, cost, 0.001);
    }

    @Test
    void multipliesTotalForExpress() {
        double cost = calculator.calculateCost("DOMESTIC", 10, "EXPRESS");

        assertEquals(75, cost, 0.001);
    }

    @Test
    void addsHeavySurchargeOverThirtyKg() {
        double cost = calculator.calculateCost("DOMESTIC", 40, "STANDARD");

        assertEquals(220, cost, 0.001);
    }

    @Test
    void appliesExpressAfterFeesAndSurcharge() {
        double cost = calculator.calculateCost("INTERNATIONAL", 40, "EXPRESS");

        assertEquals(967.5, cost, 0.001);
    }

    @Test
    void noHeavySurchargeAtExactlyThirtyKg() {
        double cost = calculator.calculateCost("DOMESTIC", 30, "STANDARD");

        assertEquals(150, cost, 0.001);
    }

    @Test
    void addsHeavySurchargeJustOverThirtyKg() {
        double cost = calculator.calculateCost("DOMESTIC", 30.1, "STANDARD");

        assertEquals(170.5, cost, 0.001);
    }

}
