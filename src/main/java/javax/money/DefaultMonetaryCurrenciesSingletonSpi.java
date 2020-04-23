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
package javax.money;

import javax.money.spi.Bootstrap;
import javax.money.spi.CurrencyProviderSpi;
import javax.money.spi.MonetaryCurrenciesSingletonSpi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory singleton for {@link javax.money.CurrencyUnit} instances as provided by the
 * different registered {@link javax.money.spi.CurrencyProviderSpi} instances.
 * <p>
 * This class is thread safe.
 *
 * @author Anatole Tresch
 * @version 0.9
 */
final class DefaultMonetaryCurrenciesSingletonSpi implements MonetaryCurrenciesSingletonSpi {

    @Override
    public Set<CurrencyUnit> getCurrencies(CurrencyQuery query) {
        Set<CurrencyUnit> result = new HashSet<>();
        List<CurrencyProviderSpi> providers = collectProviders(query);
        for (CurrencyProviderSpi spi : providers) {
            try {
                result.addAll(spi.getCurrencies(query));
            } catch (Exception e) {
                Logger.getLogger(DefaultMonetaryCurrenciesSingletonSpi.class.getName())
                        .log(Level.SEVERE, "Error loading currency provider names for " + spi.getClass().getName(),
                                e);
            }
        }
        return result;
    }

    private List<CurrencyProviderSpi> collectProviders(CurrencyQuery query) {
        List<CurrencyProviderSpi> result = new ArrayList<>();
        if (!query.getProviderNames().isEmpty()) {
            for (String providerName : query.getProviderNames()) {
                CurrencyProviderSpi provider = getProvider(providerName);
                if (provider == null) {
                    Logger.getLogger(DefaultMonetaryCurrenciesSingletonSpi.class.getName()).warning("No such currency " +
                            "provider found, ignoring: " + providerName);
                } else {
                    result.add(provider);
                }
            }
        }
        else{
            for(String providerName:getDefaultProviderChain()){
                CurrencyProviderSpi provider = getProvider(providerName);
                if (provider == null) {
                    Logger.getLogger(DefaultMonetaryCurrenciesSingletonSpi.class.getName()).warning("No such currency " +
                            "provider found, ignoring: " + providerName);
                } else {
                    result.add(provider);
                }
            }
        }
        return result;
    }

    private CurrencyProviderSpi getProvider(String providerName) {
        for(CurrencyProviderSpi provider:Bootstrap.getServices(CurrencyProviderSpi.class)){
            if(provider.getProviderName().equals(providerName)){
                return provider;
            }
        }
        return null;
    }

    /**
     * This default implementation simply returns all providers defined in arbitrary order.
     *
     * @return the default provider chain, never null.
     */
    @Override
    public List<String> getDefaultProviderChain() {
        List<String> provList = new ArrayList<>();
        for(CurrencyProviderSpi currencyProviderSpi:Bootstrap.getServices(CurrencyProviderSpi.class)){
            provList.add(currencyProviderSpi.getProviderName());
        }
        return provList;
    }

    /**
     * Get the names of the currently loaded providers.
     *
     * @return the names of the currently loaded providers, never null.
     */
    @Override
    public Set<String> getProviderNames() {
        Set<String> result = new HashSet<>();
        for (CurrencyProviderSpi spi : Bootstrap.getServices(CurrencyProviderSpi.class)) {
            try {
                result.add(spi.getProviderName());
            } catch (Exception e) {
                Logger.getLogger(DefaultMonetaryCurrenciesSingletonSpi.class.getName())
                        .log(Level.SEVERE, "Error loading currency provider names for " + spi.getClass().getName(),
                                e);
            }
        }
        return result;
    }

    /**
     * Access a new instance based on the currency code. Currencies are
     * available as provided by {@link javax.money.spi.CurrencyProviderSpi} instances registered
     * with the {@link javax.money.spi.Bootstrap}.
     *
     * @param currencyCode the ISO currency code, not {@code null}.
     * @param providers    the (optional) specification of providers to consider. If not set (empty) the providers
     *                     as defined by #getDefaultRoundingProviderChain() should be used.
     * @return the corresponding {@link javax.money.CurrencyUnit} instance.
     * @throws javax.money.UnknownCurrencyException if no such currency exists.
     */
    public CurrencyUnit getCurrency(String currencyCode, String... providers) {
        Objects.requireNonNull(currencyCode, "Currency Code may not be null");
        Collection<CurrencyUnit> found =
                getCurrencies(CurrencyQueryBuilder.of().setCurrencyCodes(currencyCode).setProviderNames(providers).build());
        if (found.isEmpty()) {
            throw new UnknownCurrencyException(currencyCode);
        }
        if (found.size() > 1) {
            throw new MonetaryException("Ambiguous CurrencyUnit for code: " + currencyCode + ": " + found);
        }
        return found.iterator().next();
    }

    /**
     * Access a new instance based on the currency code. Currencies are
     * available as provided by {@link javax.money.spi.CurrencyProviderSpi} instances registered
     * with the {@link javax.money.spi.Bootstrap}.
     *
     * @param country   the ISO currency's country, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultRoundingProviderChain() should be used.
     * @return the corresponding {@link javax.money.CurrencyUnit} instance.
     * @throws javax.money.UnknownCurrencyException if no such currency exists.
     */
    public CurrencyUnit getCurrency(Locale country, String... providers) {
        Collection<CurrencyUnit> found =
                getCurrencies(CurrencyQueryBuilder.of().setCountries(country).setProviderNames(providers).build());
        if (found.isEmpty()) {
            throw new MonetaryException("No currency unit found for locale: " + country);
        }
        if (found.size() > 1) {
            throw new MonetaryException("Ambiguous CurrencyUnit for locale: " + country + ": " + found);
        }
        return found.iterator().next();
    }

    /**
     * Provide access to all currently known currencies.
     *
     * @param locale    the target {@link java.util.Locale}, typically representing an ISO country,
     *                  not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultRoundingProviderChain() should be used.
     * @return a collection of all known currencies, never null.
     */
    public Set<CurrencyUnit> getCurrencies(Locale locale, String... providers) {
        return getCurrencies(CurrencyQueryBuilder.of().setCountries(locale).setProviderNames(providers).build());
    }

    /**
     * Allows to check if a {@link javax.money.CurrencyUnit} instance is defined, i.e.
     * accessible from {@link javax.money.spi.MonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}.
     *
     * @param code      the currency code, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultRoundingProviderChain() should be used.
     * @return {@code true} if {@link javax.money.spi.MonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}
     * would return a result for the given code.
     */
    public boolean isCurrencyAvailable(String code, String... providers) {
        return !getCurrencies(CurrencyQueryBuilder.of().setCurrencyCodes(code).setProviderNames(providers).build())
                .isEmpty();
    }

    /**
     * Allows to check if a {@link javax.money.CurrencyUnit} instance is
     * defined, i.e. accessible from {@link #getCurrency(String, String...)}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultRoundingProviderChain() should be used.
     * @return {@code true} if {@link #getCurrencies(java.util.Locale, String...)} would return a
     * non empty result for the given code.
     */
    public boolean isCurrencyAvailable(Locale locale, String... providers) {
        return !getCurrencies(CurrencyQueryBuilder.of().setCountries(locale).setProviderNames(providers).build()).isEmpty();
    }

    /**
     * Provide access to all currently known currencies.
     *
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultRoundingProviderChain() should be used.
     * @return a collection of all known currencies, never null.
     */
    public Set<CurrencyUnit> getCurrencies(String... providers) {
        return getCurrencies(CurrencyQueryBuilder.of().setProviderNames(providers).build());
    }

    /**
     * Access a single currency by query.
     *
     * @param query The currency query, not null.
     * @return the {@link javax.money.CurrencyUnit} found, never null.
     * @throws javax.money.MonetaryException if multiple currencies match the query.
     */
    public CurrencyUnit getCurrency(CurrencyQuery query) {
        Set<CurrencyUnit> currencies = getCurrencies(query);
        if (currencies.isEmpty()) {
            return null;
        }
        if (currencies.size() == 1) {
            return currencies.iterator().next();
        }
        throw new MonetaryException("Ambiguous request for CurrencyUnit: " + query + ", found: " + currencies);
    }
}
