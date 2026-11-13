package com.tidewater.freight.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tidewater.freight.shipment.ServiceLevel;
import com.tidewater.freight.shipment.Shipment;
import com.tidewater.freight.tariff.InMemoryTariffRepository;
import com.tidewater.freight.tariff.PropertiesTariffLoader;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ShippingCalculatorCharacterisationTest {

    private final ShippingCalculator calculator = new ShippingCalculator(
            new InMemoryTariffRepository(
                    new PropertiesTariffLoader().loadResource("tariffs.properties")));

    @Test
    void chargesBaseRateOnlyForDomestic() {
        assertEquals(new BigDecimal("50.00"), priceOf("DOMESTIC", "10", ServiceLevel.STANDARD));
    }

    @Test
    void addsHandlingFeeForRegional() {
        assertEquals(new BigDecimal("50.00"), priceOf("REGIONAL", "5", ServiceLevel.STANDARD));
    }

    @Test
    void addsCustomsFeeForInternational() {
        assertEquals(new BigDecimal("175.00"),
                priceOf("INTERNATIONAL", "10", ServiceLevel.STANDARD));
    }

    @Test
    void multipliesTotalForExpress() {
        assertEquals(new BigDecimal("75.00"), priceOf("DOMESTIC", "10", ServiceLevel.EXPRESS));
    }

    @Test
    void addsHeavySurchargeOverThirtyKg() {
        assertEquals(new BigDecimal("220.00"), priceOf("DOMESTIC", "40", ServiceLevel.STANDARD));
    }

    @Test
    void appliesExpressAfterFeesAndSurcharge() {
        assertEquals(new BigDecimal("967.50"),
                priceOf("INTERNATIONAL", "40", ServiceLevel.EXPRESS));
    }

    @Test
    void noHeavySurchargeAtExactlyThirtyKg() {
        assertEquals(new BigDecimal("150.00"), priceOf("DOMESTIC", "30", ServiceLevel.STANDARD));
    }

    @Test
    void addsHeavySurchargeJustOverThirtyKg() {
        assertEquals(new BigDecimal("170.50"),
                priceOf("DOMESTIC", "30.1", ServiceLevel.STANDARD));
    }

    @Test
    void pricesWithoutFloatingPointError() {
        assertEquals(new BigDecimal("42.60"), priceOf("REGIONAL", "2.3", ServiceLevel.EXPRESS));
    }

    @Test
    void roundsPriceWithThreeDecimalsToTwo() {
        assertEquals(new BigDecimal("54.96"), priceOf("REGIONAL", "3.33", ServiceLevel.EXPRESS));
    }

    private BigDecimal priceOf(String zone, String weightKg, ServiceLevel serviceLevel) {
        return calculator.calculateCost(new Shipment(zone, new BigDecimal(weightKg), serviceLevel));
    }
}
