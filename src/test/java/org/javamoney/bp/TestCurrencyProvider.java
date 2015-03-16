/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE.
 * 
 * Specification: JSR-354 Money and Currency API ("Specification")
 * 
 * Copyright (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp;

import org.javamoney.bp.spi.CurrencyProviderSpi;
import java.util.*;

public final class TestCurrencyProvider implements CurrencyProviderSpi {

    @Override
    public Set<CurrencyUnit> getCurrencies(CurrencyQuery currencyQuery) {
        Set<CurrencyUnit> result = new HashSet<CurrencyUnit>();
        if (!currencyQuery.getCurrencyCodes().isEmpty()) {
            for (String currencyCode : currencyQuery.getCurrencyCodes()) {
                if ("test1".equals(currencyCode)) {
                    result.add(new TestCurrency("test1", 1, 2));
                }
                else if ("test2".equals(currencyCode)) {
                    result.add(new TestCurrency("test2", 1, 2));
                }
                else if ("error".equals(currencyCode)) {
                    throw new IllegalArgumentException("error encountered!");
                }
                else if ("invalid".equals(currencyCode)) {
                    result.add(new TestCurrency("invalid2", 1, 2));
                }
            }
            return result;
        }
        if (!currencyQuery.getCountries().isEmpty()) {
            for (Locale country : currencyQuery.getCountries()) {
                if ("TEST1L".equals(country.getCountry())) {
                    result.add(new TestCurrency("TEST1L", 1, 2));
                } else if (Locale.CHINA.equals(country)) {
                    throw new IllegalArgumentException("CHINA error encountered!");
                } else if (Locale.CHINESE.equals(country)) {
                    result.add(new TestCurrency("invalid2", 1, 2));
                }
            }
            return result;
        }
        return Collections.emptySet();
    }

    @Override
    public boolean isCurrencyAvailable(CurrencyQuery context) {
        Collection<String> currencyCodes = context.getCurrencyCodes();
        for (String currencyCode : currencyCodes) {
            if ("test1".equals(currencyCode) || "error".equals(currencyCode) || "invalid".equals(currencyCode)) {
                    return true;
            }
        }
        Collection<Locale> countries = context.getCountries();
        for (Locale country : countries) {
            if ("TEST1L".equals(country.getCountry())) {
                return true;
            } else if (Locale.CHINA.equals(country)) {
                return true;
            } else if (Locale.CHINESE.equals(country)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String getProviderName() {
        return "test";
    }


    private static final class TestCurrency implements CurrencyUnit {

        private String code;
        private int numCode;
        private int digits;
        private static final CurrencyContext CONTEXT = CurrencyContextBuilder.of("TestCurrencyProvider").build();

        public TestCurrency(String code, int numCode, int digits) {
            this.code = code;
            this.numCode = numCode;
            this.digits = digits;
        }

        @Override
        public String getCurrencyCode() {
            return code;
        }

        @Override
        public int getNumericCode() {
            return numCode;
        }

        @Override
        public int getDefaultFractionDigits() {
            return digits;
        }

        @Override
        public CurrencyContext getContext() {
            return CONTEXT;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(code);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof TestCurrency) {
                TestCurrency other = (TestCurrency) obj;
                return Objects.equals(code, other.code);
            }
            return false;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public int compareTo(CurrencyUnit o) {
            Objects.requireNonNull(o);
            return getCurrencyCode().compareTo(o.getCurrencyCode());
        }
    }

}
