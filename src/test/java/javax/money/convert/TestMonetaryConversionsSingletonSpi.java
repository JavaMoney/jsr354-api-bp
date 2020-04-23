/*
 * Copyright 2012-2016 Credit Suisse
 * Copyright 2018-2020 Werner Keil, Otavio Santana, Trivadis AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.money.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryException;
import javax.money.spi.MonetaryConversionsSingletonSpi;

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
        return new ArrayList<>(getProviderNames());
    }

    /**
     * Allows to quickly check, if a {@link CurrencyConversion} is accessible for the given
     * {@link ConversionQuery}.
     *
     * @param termCurrency the terminating/target currency unit, not null.
     * @param providers    the provider names defines a corresponding
     *                     provider chain that must be encapsulated by the resulting {@link javax
     *                     .money.convert.CurrencyConversion}. By default the provider
     *                     chain as defined by #getDefaultRoundingProviderChain will be used.
     * @return {@code true}, if such a conversion is supported, meaning an according
     * {@link CurrencyConversion} can be
     * accessed.
     * @see #getConversion(ConversionQuery)
     * @see #getConversion(javax.money.CurrencyUnit, String...)}
     */
    public boolean isConversionAvailable(CurrencyUnit termCurrency, String... providers) {
        return isConversionAvailable(
                ConversionQueryBuilder.of().setTermCurrency(termCurrency).setProviderNames(providers).build());
    }

    /**
     * Access the current registered {@link ExchangeRateProvider} instances. If no provider
     * names are passed ALL current registered providers are returned in undefined order.
     *
     * @param providers the provider names of hte providers to be accessed
     * @return the list of providers, in the same order as requested.
     * @throws javax.money.MonetaryException if a provider could not be resolved.
     */
    public List<ExchangeRateProvider> getExchangeRateProviders(String... providers) {
        List<ExchangeRateProvider> provInstances = new ArrayList<>();
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
     * Access a compound instance of an {@link ExchangeRateProvider} based on the given provider chain.
     *
     * @param providers the {@link ConversionQuery} provider names defines a corresponding
     *                  prpovider chain that must be
     *                  encapsulated by the resulting {@link ExchangeRateProvider}. By default
     *                  the default
     *                  provider changes as defined in #getDefaultRoundingProviderChain will be used.
     * @return an {@link ExchangeRateProvider} built up with the given sub
     * providers, never {@code null}.
     * @throws javax.money.MonetaryException if a provider listed could not be found.
     * @see #getProviderNames()
     * @see #isExchangeRateProviderAvailable(ConversionQuery)
     */
    public ExchangeRateProvider getExchangeRateProvider(String... providers) {
        return getExchangeRateProvider(ConversionQueryBuilder.of().setProviderNames(providers).build());
    }

    /**
     * Access an instance of {@link CurrencyConversion}.
     *
     * @param conversionQuery the {@link ConversionQuery} determining the type of conversion
     *                        required, not null.
     * @return the corresponding conversion, not null.
     * @throws javax.money.MonetaryException if no matching conversion could be found.
     * @see #isConversionAvailable(ConversionQuery)
     */
    public CurrencyConversion getConversion(ConversionQuery conversionQuery) {
        return getExchangeRateProvider(conversionQuery).getCurrencyConversion(
                Objects.requireNonNull(conversionQuery.getCurrency(), "Terminating Currency is required.")
        );
    }

    /**
     * Access an instance of {@link CurrencyConversion}.
     *
     * @param termCurrency the terminating/target currency unit, not null.
     * @param providers    the {@link ConversionQuery} provider names defines a corresponding
     *                     prpovider chain that must be encapsulated by the resulting {@link javax
     *                     .money.convert.CurrencyConversion}. By default the default
     *                     provider chain as defined by #getDefaultRoundingProviderChain will be used.
     * @return the corresponding conversion, not null.
     * @throws javax.money.MonetaryException if no matching conversion could be found.
     * @see #isConversionAvailable(ConversionQuery)
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
         * Access a {@link ExchangeRate} using the given currencies. The
         * {@link ExchangeRate} may be, depending on the data provider, eal-time or
         * deferred. This method should return the rate that is <i>currently</i>
         * valid.
         *
         * @param base base {@link javax.money.CurrencyUnit}, not {@code null}
         * @param term term {@link javax.money.CurrencyUnit}, not {@code null}
         * @throws CurrencyConversionException If no such rate is available.
         */
        public ExchangeRate getExchangeRate(CurrencyUnit base, CurrencyUnit term){
            Objects.requireNonNull(base, "Base Currency is null");
            Objects.requireNonNull(term, "Term Currency is null");
            return getExchangeRate(ConversionQueryBuilder.of().setBaseCurrency(base).setTermCurrency(term).build());
        }

        /**
         * Access a {@link CurrencyConversion} that can be applied as a
         * {@link javax.money.MonetaryOperator} to an amount.
         *
         * @param term term {@link javax.money.CurrencyUnit}, not {@code null}
         * @return a new instance of a corresponding {@link CurrencyConversion},
         * never {@code null}.
         */
        public CurrencyConversion getCurrencyConversion(CurrencyUnit term){
            return getCurrencyConversion(ConversionQueryBuilder.of().setTermCurrency(term).build());
        }

        /**
         * Checks if an {@link ExchangeRate} between two {@link javax.money.CurrencyUnit} is
         * available from this provider. This method should check, if a given rate
         * is <i>currently</i> defined.
         *
         * @param baseCode the base currency code
         * @param termCode the terminal/target currency code
         * @return {@code true}, if such an {@link ExchangeRate} is currently
         * defined.
         * @throws javax.money.MonetaryException if one of the currency codes passed is not valid.
         */
        public boolean isAvailable(String baseCode, String termCode){
            return isAvailable(Monetary.getCurrency(baseCode), Monetary.getCurrency(termCode));
        }


        /**
         * Access a {@link ExchangeRate} using the given currencies. The
         * {@link ExchangeRate} may be, depending on the data provider, eal-time or
         * deferred. This method should return the rate that is <i>currently</i>
         * valid.
         *
         * @param baseCode base currency code, not {@code null}
         * @param termCode term/target currency code, not {@code null}
         * @return the matching {@link ExchangeRate}.
         * @throws CurrencyConversionException If no such rate is available.
         * @throws javax.money.MonetaryException           if one of the currency codes passed is not valid.
         */
        public ExchangeRate getExchangeRate(String baseCode, String termCode){
            return getExchangeRate(Monetary.getCurrency(baseCode), Monetary.getCurrency(termCode));
        }

        /**
         * Access a {@link CurrencyConversion} that can be applied as a
         * {@link javax.money.MonetaryOperator} to an amount.
         *
         * @param termCode terminal/target currency code, not {@code null}
         * @return a new instance of a corresponding {@link CurrencyConversion},
         * never {@code null}.
         * @throws javax.money.MonetaryException if one of the currency codes passed is not valid.
         */
        public CurrencyConversion getCurrencyConversion(String termCode){
            return getCurrencyConversion(Monetary.getCurrency(termCode));
        }

    }


}
