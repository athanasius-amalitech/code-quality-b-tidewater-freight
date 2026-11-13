package com.tidewater.freight;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tidewater.freight.exceptions.UnknownZoneException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShippingCalculatorTest {

    @Mock
    private TariffRepository tariffRepository;

    @Test
    void pricesWithTariffFromRepository() {

        when(tariffRepository.findTariff("DOMESTIC"))
                .thenReturn(new FlatRateTariff(new BigDecimal("2"), new BigDecimal("1")));

        Shipment shipment = new Shipment("DOMESTIC", 10, ServiceLevel.STANDARD);

        ShippingCalculator calculator = new ShippingCalculator(tariffRepository);

        BigDecimal cost = calculator.calculateCost(shipment);

        assertEquals(new BigDecimal("21.00"), cost);
    }

    @Test
    void letsUnknownZoneFailurePropagate() {
        when(tariffRepository.findTariff("MOON")).thenThrow(new UnknownZoneException("MOON"));

        ShippingCalculator calculator = new ShippingCalculator(tariffRepository);

        assertThrows(UnknownZoneException.class,
                () -> calculator.calculateCost(new Shipment("MOON", 10, ServiceLevel.STANDARD)));
    }
}
