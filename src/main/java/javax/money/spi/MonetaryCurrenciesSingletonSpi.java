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
package javax.money.spi;

import javax.money.CurrencyQuery;
import javax.money.CurrencyUnit;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Factory singleton backing interface for {@link javax.money.MonetaryCurrencies} that provides access to
 * different registered {@link javax.money.spi.CurrencyProviderSpi} instances.
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
     * available as provided by {@link javax.money.spi.CurrencyProviderSpi} instances registered
     * with the {@link Bootstrap}.
     *
     * @param currencyCode the ISO currency code, not {@code null}.
     * @param providers    the (optional) specification of providers to consider. If not set (empty) the providers
     *                     as defined by #getDefaultProviderChain() should be used.
     * @return the corresponding {@link javax.money.CurrencyUnit} instance.
     * @throws javax.money.UnknownCurrencyException if no such currency exists.
     */
    CurrencyUnit getCurrency(String currencyCode, String... providers);

    /**
     * Access a new instance based on the currency code. Currencies are
     * available as provided by {@link javax.money.spi.CurrencyProviderSpi} instances registered
     * with the {@link Bootstrap}.
     *
     * @param country   the ISO currency's country, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultProviderChain() should be used.
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
     *                  as defined by #getDefaultProviderChain() should be used.
     * @return a collection of all known currencies, never null.
     */
    Set<CurrencyUnit> getCurrencies(Locale locale, String... providers);

    /**
     * Allows to check if a {@link javax.money.CurrencyUnit} instance is defined, i.e.
     * accessible from {@link javax.money.spi.MonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}.
     *
     * @param code      the currency code, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultProviderChain() should be used.
     * @return {@code true} if {@link javax.money.spi.MonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}
     * would return a result for the given code.
     */
    boolean isCurrencyAvailable(String code, String... providers);

    /**
     * Allows to check if a {@link javax.money.CurrencyUnit} instance is
     * defined, i.e. accessible from {@link #getCurrency(String, String...)}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultProviderChain() should be used.
     * @return {@code true} if {@link #getCurrencies(java.util.Locale, String...)} would return a
     * non empty result for the given code.
     */
    boolean isCurrencyAvailable(Locale locale, String... providers);

    /**
     * Provide access to all currently known currencies.
     *
     * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
     *                  as defined by #getDefaultProviderChain() should be used.
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
