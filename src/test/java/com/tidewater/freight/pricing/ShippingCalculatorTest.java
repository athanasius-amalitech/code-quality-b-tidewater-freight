package com.tidewater.freight.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.tidewater.freight.shipment.ServiceLevel;
import com.tidewater.freight.shipment.Shipment;
import com.tidewater.freight.tariff.FlatRateTariff;
import com.tidewater.freight.tariff.TariffRepository;
import com.tidewater.freight.tariff.UnknownZoneException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void rejectsMissingRepository() {
        assertThrows(NullPointerException.class, () -> new ShippingCalculator(null));
    }
}
