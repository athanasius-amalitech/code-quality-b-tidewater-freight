package com.tidewater.freight.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.tidewater.freight.shipment.ServiceLevel;
import com.tidewater.freight.shipment.Shipment;
import com.tidewater.freight.tariff.FlatRateTariff;
import com.tidewater.freight.tariff.TariffRepository;
import com.tidewater.freight.tariff.UnknownZoneException;
import com.tidewater.freight.tariff.ZoneTariff;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShippingCalculatorTest {

    @Mock
    private TariffRepository tariffRepository;

    @Mock
    private ZoneTariff zoneTariff;

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
    void addsHeavySurchargeOnTopOfAnyTariff() {
        when(tariffRepository.findTariff("COASTAL")).thenReturn(zoneTariff);
        when(zoneTariff.costFor(30.1)).thenReturn(new BigDecimal("100"));

        ShippingCalculator calculator = new ShippingCalculator(tariffRepository);

        BigDecimal cost = calculator
                .calculateCost(new Shipment("COASTAL", 30.1, ServiceLevel.STANDARD));

        assertEquals(new BigDecimal("120.00"), cost);
    }

    @Test
    void appliesExpressToTariffPlusSurcharge() {
        when(tariffRepository.findTariff("COASTAL")).thenReturn(zoneTariff);
        when(zoneTariff.costFor(40)).thenReturn(new BigDecimal("100"));

        ShippingCalculator calculator = new ShippingCalculator(tariffRepository);

        BigDecimal cost = calculator
                .calculateCost(new Shipment("COASTAL", 40, ServiceLevel.EXPRESS));

        assertEquals(new BigDecimal("180.00"), cost);
    }

    @Test
    void roundsTariffCostHalfUpToTwoDecimals() {
        when(tariffRepository.findTariff("COASTAL")).thenReturn(zoneTariff);
        when(zoneTariff.costFor(10)).thenReturn(new BigDecimal("10.005"));

        ShippingCalculator calculator = new ShippingCalculator(tariffRepository);

        BigDecimal cost = calculator
                .calculateCost(new Shipment("COASTAL", 10, ServiceLevel.STANDARD));

        assertEquals(new BigDecimal("10.01"), cost);
    }

    @Test
    void rejectsMissingRepository() {
        assertThrows(NullPointerException.class, () -> new ShippingCalculator(null));
    }
}
