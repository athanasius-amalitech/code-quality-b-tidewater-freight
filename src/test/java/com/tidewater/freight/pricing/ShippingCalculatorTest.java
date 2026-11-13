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

        Shipment shipment = new Shipment("DOMESTIC", new BigDecimal("10"), ServiceLevel.STANDARD);

        ShippingCalculator calculator = new ShippingCalculator(tariffRepository);

        BigDecimal cost = calculator.calculateCost(shipment);

        assertEquals(new BigDecimal("21.00"), cost);
    }

    @Test
    void letsUnknownZoneFailurePropagate() {
        when(tariffRepository.findTariff("MOON")).thenThrow(new UnknownZoneException("MOON"));

        ShippingCalculator calculator = new ShippingCalculator(tariffRepository);
        Shipment shipment = new Shipment("MOON", new BigDecimal("10"), ServiceLevel.STANDARD);

        assertThrows(UnknownZoneException.class, () -> calculator.calculateCost(shipment));
    }

    @Test
    void addsHeavySurchargeOnTopOfAnyTariff() {
        BigDecimal cost = priceWithMockedTariff("30.1", "100", ServiceLevel.STANDARD);

        assertEquals(new BigDecimal("120.00"), cost);
    }

    @Test
    void addsNoSurchargeAtExactlyThirtyKg() {
        BigDecimal cost = priceWithMockedTariff("30", "100", ServiceLevel.STANDARD);

        assertEquals(new BigDecimal("100.00"), cost);
    }

    @Test
    void appliesExpressToTariffPlusSurcharge() {
        BigDecimal cost = priceWithMockedTariff("40", "100", ServiceLevel.EXPRESS);

        assertEquals(new BigDecimal("180.00"), cost);
    }

    @Test
    void roundsTariffCostHalfUpToTwoDecimals() {
        BigDecimal cost = priceWithMockedTariff("10", "10.005", ServiceLevel.STANDARD);

        assertEquals(new BigDecimal("10.01"), cost);
    }

    @Test
    void rejectsMissingRepository() {
        assertThrows(NullPointerException.class, () -> new ShippingCalculator(null));
    }

    private BigDecimal priceWithMockedTariff(String weightKg, String tariffCost,
            ServiceLevel serviceLevel) {
        BigDecimal weight = new BigDecimal(weightKg);
        when(tariffRepository.findTariff("COASTAL")).thenReturn(zoneTariff);
        when(zoneTariff.costFor(weight)).thenReturn(new BigDecimal(tariffCost));

        ShippingCalculator calculator = new ShippingCalculator(tariffRepository);

        return calculator.calculateCost(new Shipment("COASTAL", weight, serviceLevel));
    }
}
