package com.tidewater.freight.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ShipmentTest {

    private static final BigDecimal TEN_KG = new BigDecimal("10");

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.000", "-10"})
    void rejectsWeightThatIsNotPositive(String weightKg) {
        BigDecimal weight = new BigDecimal(weightKg);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class,
                () -> new Shipment("DOMESTIC", weight, ServiceLevel.STANDARD));

        assertTrue(exception.getMessage().contains("Weight"));
    }

    @Test
    void rejectsMissingWeight() {
        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class,
                () -> new Shipment("DOMESTIC", null, ServiceLevel.STANDARD));

        assertTrue(exception.getMessage().contains("Weight"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void rejectsMissingZone(String zone) {
        assertThrows(InvalidShipmentException.class,
                () -> new Shipment(zone, TEN_KG, ServiceLevel.STANDARD));
    }

    @Test
    void rejectsNullServiceLevel() {
        assertThrows(InvalidShipmentException.class, () -> new Shipment("DOMESTIC", TEN_KG, null));
    }

    @Test
    void acceptsValidShipment() {
        Shipment shipment = new Shipment("DOMESTIC", TEN_KG, ServiceLevel.STANDARD);

        assertEquals("DOMESTIC", shipment.zone());
        assertEquals(TEN_KG, shipment.weightKg());
        assertEquals(ServiceLevel.STANDARD, shipment.serviceLevel());
    }
}
