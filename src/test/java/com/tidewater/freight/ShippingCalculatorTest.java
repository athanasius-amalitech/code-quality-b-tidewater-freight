package com.tidewater.freight;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
