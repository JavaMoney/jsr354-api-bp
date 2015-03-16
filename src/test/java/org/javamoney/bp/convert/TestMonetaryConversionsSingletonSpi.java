/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency
 * API ("Specification") Copyright (c) 2012-2014, Credit Suisse All rights
 * reserved.
 */
package org.javamoney.bp.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.javamoney.bp.CurrencyUnit;
import org.javamoney.bp.MonetaryAmount;
import org.javamoney.bp.MonetaryCurrencies;
import org.javamoney.bp.MonetaryException;
import org.javamoney.bp.spi.MonetaryConversionsSingletonSpi;

/**
 * @author Anatole Tresch
 * @author Werner
 * @version 0.3 on 11.05.14.
 */
public class TestMonetaryConversionsSingletonSpi implements MonetaryConversionsSingletonSpi {

    private ExchangeRateProvider provider = new DummyRateProvider();


    @Override
    public ExchangeRateProvider getExchangeRateProvider(ConversionQuery conversionQuery) {

        if (conversionQuery.getProviderNames().isEmpty() || conversionQuery.getProviderNames().contains("test")) {
            return provider;
        }
        throw new MonetaryException("No such rate provider(s): " + conversionQuery.getProviderNames());
    }

    @Override
    public boolean isExchangeRateProviderAvailable(ConversionQuery conversionQuery) {
        return conversionQuery.getProviderNames().isEmpty() || conversionQuery.getProviderNames().contains("test");
    }

    @Override
    public boolean isConversionAvailable(ConversionQuery conversionQuery) {
        return conversionQuery.getProviderNames().isEmpty() || conversionQuery.getProviderNames().contains("test");
    }

    @Override
    public Collection<String> getProviderNames() {
        return Collections.singletonList("test");
    }

    @Override
    public List<String> getDefaultProviderChain() {
        return new ArrayList<String>(getProviderNames());
    }

    /**
     * Allows to quickly check, if a {@link org.javamoney.bp.convert.CurrencyConversion} is accessible for the given
     * {@link org.javamoney.bp.convert.ConversionQuery}.
     *
     * @param termCurrency the terminating/target currency unit, not null.
     * @param providers    the provider names defines a corresponding
     *                     prpovider chain that must be encapsulated by the resulting {@link javax
     *                     .money.convert.CurrencyConversion}. By default the provider
     *                     chain as defined by #getDefaultProviderChain will be used.
     * @return {@code true}, if such a conversion is supported, meaning an according
     * {@link org.javamoney.bp.convert.CurrencyConversion} can be
     * accessed.
     * @see #getConversion(org.javamoney.bp.convert.ConversionQuery)
     * @see #getConversion(org.javamoney.bp.CurrencyUnit, String...)}
     */
    public boolean isConversionAvailable(CurrencyUnit termCurrency, String... providers) {
        return isConversionAvailable(
                ConversionQueryBuilder.of().setTermCurrency(termCurrency).setProviderNames(providers).build());
    }

    /**
     * Access the current registered {@link org.javamoney.bp.convert.ExchangeRateProvider} instances. If no provider
     * names are passed ALL current registered providers are returned in undefined order.
     *
     * @param providers the provider names of hte providers to be accessed
     * @return the list of providers, in the same order as requested.
     * @throws org.javamoney.bp.MonetaryException if a provider could not be resolved.
     */
    public List<ExchangeRateProvider> getExchangeRateProviders(String... providers) {
        List<ExchangeRateProvider> provInstances = new ArrayList<ExchangeRateProvider>();
        Collection<String> providerNames = Arrays.asList(providers);
        if (providerNames.isEmpty()) {
            providerNames = getProviderNames();
        }
        for (String provName : providerNames) {
            ExchangeRateProvider provider = getExchangeRateProvider(provName);
            if(provider==null){
                throw new MonetaryException("Unsupported conversion/rate provider: " + provName);
            }
            provInstances.add(provider);
        }
        return provInstances;
    }

    /**
     * Access a compound instance of an {@link org.javamoney.bp.convert.ExchangeRateProvider} based on the given provider chain.
     *
     * @param providers the {@link org.javamoney.bp.convert.ConversionQuery} provider names defines a corresponding
     *                  prpovider chain that must be
     *                  encapsulated by the resulting {@link org.javamoney.bp.convert.ExchangeRateProvider}. By default
     *                  the default
     *                  provider changes as defined in #getDefaultProviderChain will be used.
     * @return an {@link org.javamoney.bp.convert.ExchangeRateProvider} built up with the given sub
     * providers, never {@code null}.
     * @throws org.javamoney.bp.MonetaryException if a provider listed could not be found.
     * @see #getProviderNames()
     * @see #isExchangeRateProviderAvailable(org.javamoney.bp.convert.ConversionQuery)
     */
    public ExchangeRateProvider getExchangeRateProvider(String... providers) {
        return getExchangeRateProvider(ConversionQueryBuilder.of().setProviderNames(providers).build());
    }

    /**
     * Access an instance of {@link org.javamoney.bp.convert.CurrencyConversion}.
     *
     * @param conversionQuery the {@link org.javamoney.bp.convert.ConversionQuery} determining the tpye of conversion
     *                        required, not null.
     * @return the corresponding conversion, not null.
     * @throws org.javamoney.bp.MonetaryException if no matching conversion could be found.
     * @see #isConversionAvailable(org.javamoney.bp.convert.ConversionQuery)
     */
    public CurrencyConversion getConversion(ConversionQuery conversionQuery) {
        return getExchangeRateProvider(conversionQuery).getCurrencyConversion(
                Objects.requireNonNull(conversionQuery.getCurrency(), "Terminating Currency is required.")
        );
    }

    /**
     * Access an instance of {@link org.javamoney.bp.convert.CurrencyConversion}.
     *
     * @param termCurrency the terminating/target currency unit, not null.
     * @param providers    the {@link org.javamoney.bp.convert.ConversionQuery} provider names defines a corresponding
     *                     prpovider chain that must be encapsulated by the resulting {@link javax
     *                     .money.convert.CurrencyConversion}. By default the default
     *                     provider chain as defined by #getDefaultProviderChain will be used.
     * @return the correspöonding conversion, not null.
     * @throws org.javamoney.bp.MonetaryException if no matching conversion could be found.
     * @see #isConversionAvailable(org.javamoney.bp.convert.ConversionQuery)
     */
    public CurrencyConversion getConversion(CurrencyUnit termCurrency, String... providers) {
        return getConversion(ConversionQueryBuilder.of().setTermCurrency(termCurrency).setProviderNames(providers).build());
    }

    private static final class DummyConversion implements CurrencyConversion {

        private CurrencyUnit termCurrency;
        private ConversionContext ctx = ConversionContext.of();

        public DummyConversion(CurrencyUnit termCurrency) {
            this.termCurrency = termCurrency;
        }

        @Override
        public CurrencyUnit getCurrency() {
            return termCurrency;
        }

        @Override
        public ConversionContext getContext() {
            return ctx;
        }

        @Override
        public ExchangeRate getExchangeRate(MonetaryAmount sourceAmount) {
            return new DefaultExchangeRate.Builder(getClass().getSimpleName(), RateType.OTHER)
                    .setBaseCurrency(sourceAmount.getCurrency()).setTermCurrency(termCurrency)
                    .setFactor(TestNumberValue.of(1)).build();
        }

        @Override
        public ExchangeRateProvider getExchangeRateProvider() {
            return null;
        }

        @Override
        public MonetaryAmount apply(MonetaryAmount value) {
            return value;
        }
    }

    private static final class DummyRateProvider implements ExchangeRateProvider {

        private ProviderContext ctx = ProviderContext.of("test");

        @Override
        public ProviderContext getContext() {
            return ctx;
        }

        public boolean isAvailable(CurrencyUnit base, CurrencyUnit term) {
            return false;
        }

        public boolean isAvailable(ConversionQuery conversionContext) {
            return false;
        }

        @Override
        public ExchangeRate getExchangeRate(ConversionQuery query) {
            if (query.getBaseCurrency().getCurrencyCode().equals("test1")
                    && query.getCurrency().getCurrencyCode().equals("test2")) {
                return new DefaultExchangeRate.Builder(getClass().getSimpleName(), RateType.OTHER)
                        .setBaseCurrency(query.getBaseCurrency()).setTermCurrency(query.getCurrency())
                        .setFactor(TestNumberValue.of(new BigDecimal("0.5"))).build();
            }
            if (query.getBaseCurrency().getCurrencyCode().equals("test2")
                    && query.getCurrency().getCurrencyCode().equals("test1")) {
                return new DefaultExchangeRate.Builder(getClass().getSimpleName(), RateType.OTHER)
                        .setBaseCurrency(query.getBaseCurrency()).setTermCurrency(query.getCurrency())
                        .setFactor(TestNumberValue.of(new BigDecimal("2"))).build();
            }

            return new DefaultExchangeRate.Builder(getClass().getSimpleName(), RateType.OTHER)
                    .setBaseCurrency(query.getBaseCurrency()).setTermCurrency(query.getCurrency())
                    .setFactor(TestNumberValue.of(1)).build();
        }

        @Override
        public ExchangeRate getReversed(ExchangeRate rate) {
            return getExchangeRate(rate.getCurrency(), rate.getBaseCurrency());
        }

        @Override
        public CurrencyConversion getCurrencyConversion(ConversionQuery query) {
            return new DummyConversion(query.getCurrency());
        }

        /**
         * Access a {@link org.javamoney.bp.convert.ExchangeRate} using the given currencies. The
         * {@link org.javamoney.bp.convert.ExchangeRate} may be, depending on the data provider, eal-time or
         * deferred. This method should return the rate that is <i>currently</i>
         * valid.
         *
         * @param base base {@link org.javamoney.bp.CurrencyUnit}, not {@code null}
         * @param term term {@link org.javamoney.bp.CurrencyUnit}, not {@code null}
         * @throws org.javamoney.bp.convert.CurrencyConversionException If no such rate is available.
         */
        public ExchangeRate getExchangeRate(CurrencyUnit base, CurrencyUnit term){
            Objects.requireNonNull(base, "Base Currency is null");
            Objects.requireNonNull(term, "Term Currency is null");
            return getExchangeRate(ConversionQueryBuilder.of().setBaseCurrency(base).setTermCurrency(term).build());
        }

        /**
         * Access a {@link org.javamoney.bp.convert.CurrencyConversion} that can be applied as a
         * {@link org.javamoney.bp.MonetaryOperator} to an amount.
         *
         * @param term term {@link org.javamoney.bp.CurrencyUnit}, not {@code null}
         * @return a new instance of a corresponding {@link org.javamoney.bp.convert.CurrencyConversion},
         * never {@code null}.
         */
        public CurrencyConversion getCurrencyConversion(CurrencyUnit term){
            return getCurrencyConversion(ConversionQueryBuilder.of().setTermCurrency(term).build());
        }

        /**
         * Checks if an {@link org.javamoney.bp.convert.ExchangeRate} between two {@link org.javamoney.bp.CurrencyUnit} is
         * available from this provider. This method should check, if a given rate
         * is <i>currently</i> defined.
         *
         * @param baseCode the base currency code
         * @param termCode the terminal/target currency code
         * @return {@code true}, if such an {@link org.javamoney.bp.convert.ExchangeRate} is currently
         * defined.
         * @throws org.javamoney.bp.MonetaryException if one of the currency codes passed is not valid.
         */
        public boolean isAvailable(String baseCode, String termCode){
            return isAvailable(MonetaryCurrencies.getCurrency(baseCode), MonetaryCurrencies.getCurrency(termCode));
        }


        /**
         * Access a {@link org.javamoney.bp.convert.ExchangeRate} using the given currencies. The
         * {@link org.javamoney.bp.convert.ExchangeRate} may be, depending on the data provider, eal-time or
         * deferred. This method should return the rate that is <i>currently</i>
         * valid.
         *
         * @param baseCode base currency code, not {@code null}
         * @param termCode term/target currency code, not {@code null}
         * @return the matching {@link org.javamoney.bp.convert.ExchangeRate}.
         * @throws org.javamoney.bp.convert.CurrencyConversionException If no such rate is available.
         * @throws org.javamoney.bp.MonetaryException           if one of the currency codes passed is not valid.
         */
        public ExchangeRate getExchangeRate(String baseCode, String termCode){
            return getExchangeRate(MonetaryCurrencies.getCurrency(baseCode), MonetaryCurrencies.getCurrency(termCode));
        }

        /**
         * Access a {@link org.javamoney.bp.convert.CurrencyConversion} that can be applied as a
         * {@link org.javamoney.bp.MonetaryOperator} to an amount.
         *
         * @param termCode terminal/target currency code, not {@code null}
         * @return a new instance of a corresponding {@link org.javamoney.bp.convert.CurrencyConversion},
         * never {@code null}.
         * @throws org.javamoney.bp.MonetaryException if one of the currency codes passed is not valid.
         */
        public CurrencyConversion getCurrencyConversion(String termCode){
            return getCurrencyConversion(MonetaryCurrencies.getCurrency(termCode));
        }

    }


}
