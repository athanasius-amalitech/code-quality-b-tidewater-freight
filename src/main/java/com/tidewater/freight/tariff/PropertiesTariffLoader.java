package com.tidewater.freight.tariff;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesTariffLoader {

    private static final String FIELD_SEPARATOR = ",";
    private static final int FIELDS_PER_ZONE = 2;

    public Map<String, ZoneTariff> loadResource(String resourceName) {
        InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName);
        if (input == null) {
            throw new TariffConfigurationException("Tariff file not found: " + resourceName);
        }
        return load(input, resourceName);
    }

    Map<String, ZoneTariff> load(InputStream input, String source) {
        try (input) {
            Properties properties = new Properties();
            properties.load(input);
            return toTariffs(properties, source);
        } catch (IOException e) {
            throw new TariffConfigurationException("Could not read tariffs from " + source, e);
        }
    }

    private Map<String, ZoneTariff> toTariffs(Properties properties, String source) {
        if (properties.isEmpty()) {
            throw new TariffConfigurationException("No tariffs defined in " + source);
        }
        Map<String, ZoneTariff> tariffs = new HashMap<>();
        for (String zone : properties.stringPropertyNames()) {
            tariffs.put(zone, toTariff(zone, properties.getProperty(zone), source));
        }
        return tariffs;
    }

    private ZoneTariff toTariff(String zone, String value, String source) {
        String context = "Tariff for zone " + zone + " in " + source + " ('" + value + "')";
        String[] fields = value.split(FIELD_SEPARATOR);
        if (fields.length != FIELDS_PER_ZONE) {
            throw new TariffConfigurationException(context + " must be 'ratePerKg,handlingFee'");
        }
        return new FlatRateTariff(parseAmount(fields[0], context), parseAmount(fields[1], context));
    }

    private BigDecimal parseAmount(String field, String context) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(field.trim());
        } catch (NumberFormatException e) {
            throw new TariffConfigurationException(context + " has an amount that is not a number",
                    e);
        }
        if (amount.signum() < 0) {
            throw new TariffConfigurationException(context + " has a negative amount");
        }
        return amount;
    }
}
