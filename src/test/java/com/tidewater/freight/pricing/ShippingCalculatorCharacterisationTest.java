package com.tidewater.freight.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tidewater.freight.shipment.ServiceLevel;
import com.tidewater.freight.shipment.Shipment;
import com.tidewater.freight.tariff.InMemoryTariffRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ShippingCalculatorCharacterisationTest {

    private final ShippingCalculator calculator = new ShippingCalculator(
            new InMemoryTariffRepository());

    @Test
    void chargesBaseRateOnlyForDomestic() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("DOMESTIC", 10, ServiceLevel.STANDARD));

        assertEquals(new BigDecimal("50.00"), cost);
    }

    @Test
    void addsHandlingFeeForRegional() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("REGIONAL", 5, ServiceLevel.STANDARD));

        assertEquals(new BigDecimal("50.00"), cost);
    }

    @Test
    void addsCustomsFeeForInternational() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("INTERNATIONAL", 10, ServiceLevel.STANDARD));

        assertEquals(new BigDecimal("175.00"), cost);
    }

    @Test
    void multipliesTotalForExpress() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("DOMESTIC", 10, ServiceLevel.EXPRESS));

        assertEquals(new BigDecimal("75.00"), cost);
    }

    @Test
    void addsHeavySurchargeOverThirtyKg() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("DOMESTIC", 40, ServiceLevel.STANDARD));

        assertEquals(new BigDecimal("220.00"), cost);
    }

    @Test
    void appliesExpressAfterFeesAndSurcharge() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("INTERNATIONAL", 40, ServiceLevel.EXPRESS));

        assertEquals(new BigDecimal("967.50"), cost);
    }

    @Test
    void noHeavySurchargeAtExactlyThirtyKg() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("DOMESTIC", 30, ServiceLevel.STANDARD));

        assertEquals(new BigDecimal("150.00"), cost);
    }

    @Test
    void addsHeavySurchargeJustOverThirtyKg() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("DOMESTIC", 30.1, ServiceLevel.STANDARD));

        assertEquals(new BigDecimal("170.50"), cost);
    }

    @Test
    void pricesWithoutFloatingPointError() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("REGIONAL", 2.3, ServiceLevel.EXPRESS));

        assertEquals(new BigDecimal("42.60"), cost);
    }

    @Test
    void roundsPriceWithThreeDecimalsToTwo() {
        BigDecimal cost = calculator
                .calculateCost(new Shipment("REGIONAL", 3.33, ServiceLevel.EXPRESS));

        assertEquals(new BigDecimal("54.96"), cost);
    }
}
