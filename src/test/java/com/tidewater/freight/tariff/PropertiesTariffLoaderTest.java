package com.tidewater.freight.tariff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PropertiesTariffLoaderTest {

    private final PropertiesTariffLoader loader = new PropertiesTariffLoader();

    @ParameterizedTest
    @CsvSource({"DOMESTIC, 5, 0", "REGIONAL, 8, 10", "INTERNATIONAL, 15, 25"})
    void loadsEachZoneFromTariffFile(String zone, String ratePerKg, String handlingFee) {
        Map<String, ZoneTariff> tariffs = loader.loadResource("tariffs.properties");

        assertEquals(new FlatRateTariff(new BigDecimal(ratePerKg), new BigDecimal(handlingFee)),
                tariffs.get(zone));
    }

    @Test
    void loadsOnlyTheZonesInTheFile() {
        Map<String, ZoneTariff> tariffs = loader.loadResource("tariffs.properties");

        assertEquals(Set.of("DOMESTIC", "REGIONAL", "INTERNATIONAL"), tariffs.keySet());
    }

    @Test
    void rejectsMissingFile() {
        TariffConfigurationException exception = assertThrows(TariffConfigurationException.class,
                () -> loader.loadResource("missing.properties"));

        assertTrue(exception.getMessage().contains("missing.properties"));
    }

    @Test
    void rejectsFileWithNoTariffs() {
        assertThrows(TariffConfigurationException.class, () -> loadText("# no zones yet"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"8", "8,10,5"})
    void rejectsTariffWithoutExactlyTwoAmounts(String value) {
        TariffConfigurationException exception = assertThrows(TariffConfigurationException.class,
                () -> loadText("COASTAL=" + value));

        assertTrue(exception.getMessage().contains("COASTAL"));
    }

    @Test
    void rejectsAmountThatIsNotANumberAndKeepsTheCause() {
        TariffConfigurationException exception = assertThrows(TariffConfigurationException.class,
                () -> loadText("COASTAL=twelve,15"));

        assertTrue(exception.getMessage().contains("COASTAL"));
        assertInstanceOf(NumberFormatException.class, exception.getCause());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-12,15", "12,-15"})
    void rejectsNegativeAmount(String value) {
        TariffConfigurationException exception = assertThrows(TariffConfigurationException.class,
                () -> loadText("COASTAL=" + value));

        assertTrue(exception.getMessage().contains("negative"));
    }

    @Test
    void reportsUnreadableSourceAndKeepsTheCause() {
        InputStream brokenStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("disk error");
            }
        };

        TariffConfigurationException exception = assertThrows(TariffConfigurationException.class,
                () -> loader.load(brokenStream, "broken.properties"));

        assertTrue(exception.getMessage().contains("broken.properties"));
        assertInstanceOf(IOException.class, exception.getCause());
    }

    private Map<String, ZoneTariff> loadText(String text) {
        InputStream input = new ByteArrayInputStream(text.getBytes(StandardCharsets.ISO_8859_1));
        return loader.load(input, "test.properties");
    }
}
