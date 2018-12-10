/*
 * Copyright 2012-2016 Credit Suisse
 * Copyright 2018 Werner Keil, Otavio Santana, Trivadis AG
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
package javax.money.spi;

import javax.money.CurrencyQuery;
import javax.money.CurrencyQueryBuilder;
import javax.money.CurrencyUnit;
import javax.money.MonetaryException;
import javax.money.UnknownCurrencyException;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * Factory singleton backing interface for {@link javax.money.Monetary} that provides access to
 * different registered {@link CurrencyProviderSpi} instances.
 * <p>
 * Implementations of this interface must be thread safe.
 *
 * @author Anatole Tresch
 * @version 0.8
 */
public abstract class BaseMonetaryCurrenciesSingletonSpi implements MonetaryCurrenciesSingletonSpi{

    /**
     * Access a new instance based on the currency code. Currencies are
     * available as provided by {@link CurrencyProviderSpi} instances registered
     * with the {@link Bootstrap}.
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
     * available as provided by {@link CurrencyProviderSpi} instances registered
     * with the {@link Bootstrap}.
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
     * accessible from {@link BaseMonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}.
     *
     * @param code      the currency code, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultRoundingProviderChain() should be used.
     * @return {@code true} if {@link BaseMonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}
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
