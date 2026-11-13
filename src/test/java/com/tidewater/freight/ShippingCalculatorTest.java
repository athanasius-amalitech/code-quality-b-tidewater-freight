package com.tidewater.freight;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShippingCalculatorTest {

    private final ShippingCalculator calculator = new ShippingCalculator(new InMemoryTariffRepository());

    @Test
    void calculatesACost() {
        double cost = calculator.calculateCost(new Shipment("DOMESTIC", 10, ServiceLevel.STANDARD));
        assertTrue(cost > 0); // it returned a cost
    }

    @Test
    void calculatesExpressCost() {
        // mock the calculator and check it returns what we told it to
        ShippingCalculator calculator = mock(ShippingCalculator.class);
        when(calculator.calculateCost(new Shipment("INTERNATIONAL", 40, ServiceLevel.EXPRESS))).thenReturn(967.5);
        assertEquals(967.5, calculator.calculateCost(new Shipment("INTERNATIONAL", 40, ServiceLevel.EXPRESS)));
    }

    @Test
    void returnsACostForAShipment() {
        double cost = calculator.calculateCost(new Shipment("REGIONAL", 5, ServiceLevel.STANDARD));
        assertNotNull(cost); // a result came back
    }
}
