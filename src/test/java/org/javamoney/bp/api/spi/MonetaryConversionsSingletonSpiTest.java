/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.api.spi;

import org.javamoney.bp.api.CurrencyUnit;
import org.javamoney.bp.api.MonetaryAmount;
import org.javamoney.bp.api.MonetaryCurrencies;
import org.javamoney.bp.api.MonetaryException;
import org.javamoney.bp.api.TestCurrency;
import org.javamoney.bp.api.convert.ConversionContext;
import org.javamoney.bp.api.convert.ConversionQuery;
import org.javamoney.bp.api.convert.ConversionQueryBuilder;
import org.javamoney.bp.api.convert.CurrencyConversion;
import org.javamoney.bp.api.convert.ExchangeRate;
import org.javamoney.bp.api.convert.ExchangeRateProvider;
import org.javamoney.bp.api.convert.ProviderContext;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.AssertJUnit.*;

/**
 * Tests the default methods on MonetaryAmountsSingletonSpi.
 */
public class MonetaryConversionsSingletonSpiTest {

    private MonetaryConversionsSingletonSpi testSpi = new BaseMonetaryConversionsSingletonSpi() {

        @Override
        public Collection<String> getProviderNames() {
            return Arrays.asList("b", "a");
        }

        @Override
        public List<String> getDefaultProviderChain() {
            return Arrays.asList("a", "b");
        }

        @Override
        public ExchangeRateProvider getExchangeRateProvider(ConversionQuery conversionQuery) {
            if (conversionQuery.getProviderNames().contains("a")) {
                return new BaseExchangeRateProvider() {
                    @Override
                    public ProviderContext getContext() {
                        return ProviderContext.of("a");
                    }

                    @Override
                    public ExchangeRate getExchangeRate(ConversionQuery conversionQuery) {
                        return null;
                    }

                    @Override
                    public CurrencyConversion getCurrencyConversion(ConversionQuery conversionQuery) {
                        return new CurrencyConversion() {
                            @Override
                            public ConversionContext getContext() {
                                return null;
                            }

                            @Override
                            public ExchangeRate getExchangeRate(MonetaryAmount sourceAmount) {
                                return null;
                            }

                            @Override
                            public ExchangeRateProvider getExchangeRateProvider() {
                                return null;
                            }

                            @Override
                            public CurrencyUnit getCurrency() {
                                return null;
                            }

                            @Override
                            public MonetaryAmount apply(MonetaryAmount monetaryAmount) {
                                return null;
                            }
                        };
                    }
                };
            } else if (conversionQuery.getProviderNames().contains("b")) {
                return new BaseExchangeRateProvider() {
                    @Override
                    public ProviderContext getContext() {
                        return ProviderContext.of("b");
                    }

                    @Override
                    public ExchangeRate getExchangeRate(ConversionQuery conversionQuery) {
                        return null;
                    }

                    @Override
                    public CurrencyConversion getCurrencyConversion(ConversionQuery conversionQuery) {
                        return new CurrencyConversion() {
                            @Override
                            public ConversionContext getContext() {
                                return null;
                            }

                            @Override
                            public ExchangeRate getExchangeRate(MonetaryAmount sourceAmount) {
                                return null;
                            }

                            @Override
                            public ExchangeRateProvider getExchangeRateProvider() {
                                return null;
                            }

                            @Override
                            public CurrencyUnit getCurrency() {
                                return null;
                            }

                            @Override
                            public MonetaryAmount apply(MonetaryAmount monetaryAmount) {
                                return null;
                            }
                        };
                    }
                };
            }
            return null;
        }

    };

    @Test
    public void testGetExchangeRateProvider() {
        assertNotNull(testSpi.getExchangeRateProvider("a"));
        assertNull(testSpi.getExchangeRateProvider("foo"));
    }

    @Test
    public void testGetExchangeRateProviders() {
        assertFalse(testSpi.getExchangeRateProviders("a").isEmpty());
        assertFalse(testSpi.getExchangeRateProviders("b").isEmpty());
        assertFalse(testSpi.getExchangeRateProviders("a", "b").isEmpty());
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetExchangeRateProviders_BC1() {
        assertTrue(testSpi.getExchangeRateProviders("foo").isEmpty());
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetExchangeRateProviders_BC2() {
        assertTrue(testSpi.getExchangeRateProviders("foo", "a").isEmpty());
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetExchangeRateProviders_BC3() {
        assertTrue(testSpi.getExchangeRateProviders("a", "foo").isEmpty());
    }

    @Test
    public void testIsConversionAvailable() {
        assertTrue(testSpi.isConversionAvailable(ConversionQueryBuilder.of().setProviderNames("a").setTermCurrency(TestCurrency.of("CHF")).build()));
        assertTrue(testSpi.isConversionAvailable(ConversionQueryBuilder.of().setProviderNames("b").setTermCurrency(TestCurrency.of("CHF")).build()));
        assertTrue(testSpi.isConversionAvailable(ConversionQueryBuilder.of().setProviderNames("b", "b").setTermCurrency(TestCurrency.of("CHF")).build()));
        assertFalse(testSpi.isConversionAvailable(ConversionQueryBuilder.of().setProviderNames("foo").setTermCurrency(TestCurrency.of("CHF")).build()));
    }



    private static abstract class BaseExchangeRateProvider implements ExchangeRateProvider{

        /**
         * Checks if an {@link org.javamoney.bp.api.convert.ExchangeRate} between two {@link org.javamoney.bp.api.CurrencyUnit} is
         * available from this provider. This method should check, if a given rate
         * is <i>currently</i> defined.
         *
         * @param conversionQuery the required {@link org.javamoney.bp.api.convert.ConversionQuery}, not {@code null}
         * @return {@code true}, if such an {@link org.javamoney.bp.api.convert.ExchangeRate} is currently
         * defined.
         */
        public boolean isAvailable(ConversionQuery conversionQuery){
            Objects.requireNonNull(conversionQuery);
            try{
                return conversionQuery.getProviderNames().isEmpty() ||
                        conversionQuery.getProviderNames().contains(getContext().getProviderName());
            }
            catch(Exception e){
                return false;
            }
        }


        /**
         * Access a {@link org.javamoney.bp.api.convert.ExchangeRate} using the given currencies. The
         * {@link org.javamoney.bp.api.convert.ExchangeRate} may be, depending on the data provider, eal-time or
         * deferred. This method should return the rate that is <i>currently</i>
         * valid.
         *
         * @param base base {@link org.javamoney.bp.api.CurrencyUnit}, not {@code null}
         * @param term term {@link org.javamoney.bp.api.CurrencyUnit}, not {@code null}
         * @throws org.javamoney.bp.api.convert.CurrencyConversionException If no such rate is available.
         */
        public ExchangeRate getExchangeRate(CurrencyUnit base, CurrencyUnit term){
            Objects.requireNonNull(base, "Base Currency is null");
            Objects.requireNonNull(term, "Term Currency is null");
            return getExchangeRate(ConversionQueryBuilder.of().setBaseCurrency(base).setTermCurrency(term).build());
        }

        /**
         * Access a {@link org.javamoney.bp.api.convert.CurrencyConversion} that can be applied as a
         * {@link org.javamoney.bp.api.MonetaryOperator} to an amount.
         *
         * @param term term {@link org.javamoney.bp.api.CurrencyUnit}, not {@code null}
         * @return a new instance of a corresponding {@link org.javamoney.bp.api.convert.CurrencyConversion},
         * never {@code null}.
         */
        public CurrencyConversion getCurrencyConversion(CurrencyUnit term){
            return getCurrencyConversion(ConversionQueryBuilder.of().setTermCurrency(term).build());
        }

        /**
         * Checks if an {@link org.javamoney.bp.api.convert.ExchangeRate} between two {@link org.javamoney.bp.api.CurrencyUnit} is
         * available from this provider. This method should check, if a given rate
         * is <i>currently</i> defined.
         *
         * @param base the base {@link org.javamoney.bp.api.CurrencyUnit}
         * @param term the term {@link org.javamoney.bp.api.CurrencyUnit}
         * @return {@code true}, if such an {@link org.javamoney.bp.api.convert.ExchangeRate} is currently
         * defined.
         */
        public boolean isAvailable(CurrencyUnit base, CurrencyUnit term){
            return isAvailable(ConversionQueryBuilder.of().setBaseCurrency(base).setTermCurrency(term).build());
        }


        /**
         * Checks if an {@link org.javamoney.bp.api.convert.ExchangeRate} between two {@link org.javamoney.bp.api.CurrencyUnit} is
         * available from this provider. This method should check, if a given rate
         * is <i>currently</i> defined.
         *
         * @param baseCode the base currency code
         * @param termCode the terminal/target currency code
         * @return {@code true}, if such an {@link org.javamoney.bp.api.convert.ExchangeRate} is currently
         * defined.
         * @throws org.javamoney.bp.api.MonetaryException if one of the currency codes passed is not valid.
         */
        public boolean isAvailable(String baseCode, String termCode){
            return isAvailable(MonetaryCurrencies.getCurrency(baseCode), MonetaryCurrencies.getCurrency(termCode));
        }


        /**
         * Access a {@link org.javamoney.bp.api.convert.ExchangeRate} using the given currencies. The
         * {@link org.javamoney.bp.api.convert.ExchangeRate} may be, depending on the data provider, eal-time or
         * deferred. This method should return the rate that is <i>currently</i>
         * valid.
         *
         * @param baseCode base currency code, not {@code null}
         * @param termCode term/target currency code, not {@code null}
         * @return the matching {@link org.javamoney.bp.api.convert.ExchangeRate}.
         * @throws org.javamoney.bp.api.convert.CurrencyConversionException If no such rate is available.
         * @throws org.javamoney.bp.api.MonetaryException           if one of the currency codes passed is not valid.
         */
        public ExchangeRate getExchangeRate(String baseCode, String termCode){
            return getExchangeRate(MonetaryCurrencies.getCurrency(baseCode), MonetaryCurrencies.getCurrency(termCode));
        }


        /**
         * The method reverses the {@link org.javamoney.bp.api.convert.ExchangeRate} to a rate mapping from term
         * to base {@link org.javamoney.bp.api.CurrencyUnit}. Hereby the factor must <b>not</b> be
         * recalculated as {@code 1/oldFactor}, since typically reverse rates are
         * not symmetric in most cases.
         *
         * @return the matching reversed {@link org.javamoney.bp.api.convert.ExchangeRate}, or {@code null}, if
         * the rate cannot be reversed.
         */
        public ExchangeRate getReversed(ExchangeRate rate){
            ConversionQuery reverseQuery = rate.getContext().toQueryBuilder().setBaseCurrency(rate.getCurrency())
                    .setTermCurrency(rate.getBaseCurrency()).build();
            if(isAvailable(reverseQuery)){
                return getExchangeRate(reverseQuery);
            }
            return null;
        }


        /**
         * Access a {@link org.javamoney.bp.api.convert.CurrencyConversion} that can be applied as a
         * {@link org.javamoney.bp.api.MonetaryOperator} to an amount.
         *
         * @param termCode terminal/target currency code, not {@code null}
         * @return a new instance of a corresponding {@link org.javamoney.bp.api.convert.CurrencyConversion},
         * never {@code null}.
         * @throws org.javamoney.bp.api.MonetaryException if one of the currency codes passed is not valid.
         */
        public CurrencyConversion getCurrencyConversion(String termCode){
            return getCurrencyConversion(MonetaryCurrencies.getCurrency(termCode));
        }

    }
}
