package com.tidewater.freight.tariff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;

class InMemoryTariffRepositoryTest {

    private static final ZoneTariff COASTAL_TARIFF = new FlatRateTariff(new BigDecimal("12"),
            new BigDecimal("15"));

    private final InMemoryTariffRepository tariffRepository = new InMemoryTariffRepository(
            Map.of("COASTAL", COASTAL_TARIFF));

    @Test
    void returnsTariffForKnownZone() {
        assertEquals(COASTAL_TARIFF, tariffRepository.findTariff("COASTAL"));
    }

    @Test
    void rejectsUnknownZone() {
        UnknownZoneException exception = assertThrows(UnknownZoneException.class,
                () -> tariffRepository.findTariff("MOON"));

        assertEquals("MOON", exception.zone());
    }
}
