package com.tidewater.freight.tariff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class InMemoryTariffRepositoryTest {

    private final InMemoryTariffRepository tariffRepository = new InMemoryTariffRepository();

    @Test
    void rejectsUnknownZone() {
        UnknownZoneException exception = assertThrows(UnknownZoneException.class,
                () -> tariffRepository.findTariff("MOON"));

        assertEquals("MOON", exception.getZone());
    }
}
