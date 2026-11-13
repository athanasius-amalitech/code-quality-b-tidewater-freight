package com.tidewater.freight.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ShipmentTest {

    @ParameterizedTest
    @ValueSource(doubles = { 0, -10, Double.NaN, Double.POSITIVE_INFINITY })
    void rejectsWeightThatIsNotPositiveAndFinite(double weightKg) {
        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class,
                () -> new Shipment("DOMESTIC", weightKg, ServiceLevel.STANDARD));

        assertTrue(exception.getMessage().contains("Weight"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "   " })
    void rejectsMissingZone(String zone) {
        assertThrows(
                InvalidShipmentException.class,
                () -> new Shipment(zone, 10, ServiceLevel.STANDARD));
    }

    @Test
    void rejectsNullServiceLevel() {
        assertThrows(
                InvalidShipmentException.class,
                () -> new Shipment("DOMESTIC", 10, null));
    }

    @Test
    void acceptsValidShipment() {
        Shipment shipment = new Shipment("DOMESTIC", 10, ServiceLevel.STANDARD);

        assertEquals("DOMESTIC", shipment.zone());
        assertEquals(10, shipment.weightKg());
        assertEquals(ServiceLevel.STANDARD, shipment.serviceLevel());
    }
}
