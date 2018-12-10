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
import javax.money.CurrencyUnit;
import java.util.List;
import java.util.Locale;
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
public interface MonetaryCurrenciesSingletonSpi {

    /**
     * Access a list of the currently registered default providers. The default providers are used, when
     * no provider names are passed by the caller.
     *
     * @return the currencies returned by the given provider chain. If not provider names are provided
     * the default provider chain configured in {@code javamoney.properties} is used.
     * @see #getCurrencies(String...)
     * @see javax.money.CurrencyQueryBuilder
     */
    List<String> getDefaultProviderChain();

    /**
     * Access a list of the currently registered providers. Th names can be used to
     * access subsets of the overall currency range by calling {@link #getCurrencies(String...)}.
     *
     * @return the currencies returned by the given provider chain. If not provider names are provided
     * the default provider chain configured in {@code javamoney.properties} is used.
     */
    Set<String> getProviderNames();

    /**
     * Access all currencies matching the given query.
     *
     * @param query The currency query, not null.
     * @return a set of all currencies found, never null.
     */
    Set<CurrencyUnit> getCurrencies(CurrencyQuery query);


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
    CurrencyUnit getCurrency(String currencyCode, String... providers);

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
    CurrencyUnit getCurrency(Locale country, String... providers);

    /**
     * Provide access to all currently known currencies.
     *
     * @param locale    the target {@link java.util.Locale}, typically representing an ISO country,
     *                  not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultRoundingProviderChain() should be used.
     * @return a collection of all known currencies, never null.
     */
    Set<CurrencyUnit> getCurrencies(Locale locale, String... providers);

    /**
     * Allows to check if a {@link javax.money.CurrencyUnit} instance is defined, i.e.
     * accessible from {@link MonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}.
     *
     * @param code      the currency code, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultRoundingProviderChain() should be used.
     * @return {@code true} if {@link MonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}
     * would return a result for the given code.
     */
    boolean isCurrencyAvailable(String code, String... providers);

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
    boolean isCurrencyAvailable(Locale locale, String... providers);

    /**
     * Provide access to all currently known currencies.
     *
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultRoundingProviderChain() should be used.
     * @return a collection of all known currencies, never null.
     */
    Set<CurrencyUnit> getCurrencies(String... providers);

    /**
     * Access a single currency by query.
     *
     * @param query The currency query, not null.
     * @return the {@link javax.money.CurrencyUnit} found, never null.
     * @throws javax.money.MonetaryException if multiple currencies match the query.
     */
    CurrencyUnit getCurrency(CurrencyQuery query);
}
