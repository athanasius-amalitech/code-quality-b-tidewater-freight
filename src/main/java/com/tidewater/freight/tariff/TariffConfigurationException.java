package com.tidewater.freight.tariff;

import com.tidewater.freight.ShippingException;

public class TariffConfigurationException extends ShippingException {

    public TariffConfigurationException(String message) {
        super(message);
    }

    public TariffConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
