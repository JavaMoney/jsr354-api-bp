/*
 * Copyright 2012-2016 Credit Suisse
 * Copyright 2018-2019 Werner Keil, Otavio Santana, Trivadis AG
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
import javax.money.spi.MonetaryAmountsSingletonQuerySpi;
import javax.money.spi.MonetaryAmountsSingletonSpi;
import javax.money.spi.MonetaryCurrenciesSingletonSpi;
import javax.money.spi.MonetaryRoundingsSingletonSpi;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory singleton for {@link CurrencyUnit} instances as provided by the
 * different registered {@link CurrencyProviderSpi} instances.
 * <p>
 * This class is thread safe.
 *
 * @author Anatole Tresch
 * @version 0.8
 */
public final class Monetary {
    /**
     * The used {@link javax.money.spi.MonetaryCurrenciesSingletonSpi} instance.
     */
    private static MonetaryCurrenciesSingletonSpi monetaryCurrenciesSpi() {
        try {
            MonetaryCurrenciesSingletonSpi spi = Bootstrap
                    .getService(MonetaryCurrenciesSingletonSpi.class);
            if(spi==null) {
                spi = new DefaultMonetaryCurrenciesSingletonSpi();
            }
            return spi;
        } catch (Exception e) {
            Logger.getLogger(Monetary.class.getName())
                    .log(Level.INFO, "Failed to load MonetaryCurrenciesSingletonSpi, using default.", e);
            return new DefaultMonetaryCurrenciesSingletonSpi();
        }
    }

    /**
     * The used {@link javax.money.spi.MonetaryAmountsSingletonSpi} instance.
     */
    private static MonetaryAmountsSingletonSpi monetaryAmountsSingletonSpi() {
        MonetaryAmountsSingletonSpi spi = null;
        try {
            spi = Bootstrap.getService(MonetaryAmountsSingletonSpi.class);
        } catch (Exception e) {
            Logger.getLogger(Monetary.class.getName())
                    .log(Level.SEVERE, "Failed to load MonetaryAmountsSingletonSpi.", e);
        }
        if(spi ==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonSpi loaded, query functionality is not available.");
        }
        return spi;
    }

    /**
     * The used {@link javax.money.spi.MonetaryAmountsSingletonSpi} instance.
     */
    private static MonetaryAmountsSingletonQuerySpi monetaryAmountsSingletonQuerySpi() {
        MonetaryAmountsSingletonQuerySpi spi = null;
        try {
            spi = Bootstrap.getService(MonetaryAmountsSingletonQuerySpi.class);
        } catch (Exception e) {
            Logger.getLogger(Monetary.class.getName())
                    .log(Level.SEVERE, "Failed to load MonetaryAmountsSingletonQuerySpi.", e);
        }
        if(spi ==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonQuerySpi loaded, query functionality is not available.");
        }
        return spi;
    }

    /**
     * The used {@link javax.money.spi.MonetaryCurrenciesSingletonSpi} instance.
     */
    private static MonetaryRoundingsSingletonSpi monetaryRoundingsSpi() {
        try {
            MonetaryRoundingsSingletonSpi spi = Bootstrap
                    .getService(MonetaryRoundingsSingletonSpi.class);
            if(spi==null) {
                spi = new DefaultMonetaryRoundingsSingletonSpi();
            }
            return spi;
        } catch (Exception e) {
            Logger.getLogger(Monetary.class.getName())
                    .log(Level.SEVERE, "Failed to load MonetaryCurrenciesSingletonSpi, using default.", e);
            return new DefaultMonetaryRoundingsSingletonSpi();
        }
    }


    /**
     * Required for deserialization only.
     */
    private Monetary() {
    }

    /**
     * Access a new instance based on the currency code. Currencies are
     * available as provided by {@link CurrencyProviderSpi} instances registered
     * with the {@link javax.money.spi.Bootstrap}.
     *
     * @param currencyCode the ISO currency code, not {@code null}.
     * @param providers    the (optional) specification of providers to consider.
     * @return the corresponding {@link CurrencyUnit} instance.
     * @throws UnknownCurrencyException if no such currency exists.
     */
    public static CurrencyUnit getCurrency(String currencyCode, String... providers) {
        if(monetaryCurrenciesSpi()==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi().getCurrency(currencyCode, providers);
    }

    /**
     * Access a new instance based on the {@link java.util.Locale}. Currencies are
     * available as provided by {@link CurrencyProviderSpi} instances registered
     * with the {@link javax.money.spi.Bootstrap}.
     *
     * @param locale    the target {@link java.util.Locale}, typically representing an ISO
     *                  country, not {@code null}.
     * @param providers the (optional) specification of providers to consider.
     * @return the corresponding {@link CurrencyUnit} instance.
     * @throws UnknownCurrencyException if no such currency exists.
     */
    public static CurrencyUnit getCurrency(Locale locale, String... providers) {
        if(monetaryCurrenciesSpi()==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi().getCurrency(locale, providers);
    }

    /**
     * Access a new instance based on the {@link java.util.Locale}. Currencies are
     * available as provided by {@link CurrencyProviderSpi} instances registered
     * with the {@link javax.money.spi.Bootstrap}.
     *
     * @param locale    the target {@link java.util.Locale}, typically representing an ISO
     *                  country, not {@code null}.
     * @param providers the (optional) specification of providers to consider.
     * @return the corresponding {@link CurrencyUnit} instance.
     * @throws UnknownCurrencyException if no such currency exists.
     */
    public static Set<CurrencyUnit> getCurrencies(Locale locale, String... providers) {
        if(monetaryCurrenciesSpi()==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi().getCurrencies(locale, providers);
    }

    /**
     * Allows to check if a {@link CurrencyUnit} instance is defined, i.e.
     * accessible from {@link Monetary#getCurrency(String, String...)}.
     *
     * @param code      the currency code, not {@code null}.
     * @param providers the (optional) specification of providers to consider.
     * @return {@code true} if {@link Monetary#getCurrency(String, String...)}
     * would return a result for the given code.
     */
    public static boolean isCurrencyAvailable(String code, String... providers) {
        if(monetaryCurrenciesSpi()==null){
            throw new IllegalStateException("No Monetary Spi loaded.");
        }
        return monetaryCurrenciesSpi().isCurrencyAvailable(code, providers);
    }

    /**
     * Allows to check if a {@link CurrencyUnit} instance is
     * defined, i.e. accessible from {@link #getCurrency(String, String...)}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers the (optional) specification of providers to consider.
     * @return {@code true} if {@link #getCurrencies(java.util.Locale, String...)} would return a
     * result containing a currency with the given code.
     */
    public static boolean isCurrencyAvailable(Locale locale, String... providers) {
        if(monetaryCurrenciesSpi()==null){
            throw new IllegalStateException("No Monetary Spi loaded.");
        }
        return monetaryCurrenciesSpi().isCurrencyAvailable(locale, providers);
    }

    /**
     * Access all currencies known.
     *
     * @param providers the (optional) specification of providers to consider.
     * @return the list of known currencies, never null.
     */
    public static Collection<CurrencyUnit> getCurrencies(String... providers) {
        if(monetaryCurrenciesSpi()==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi().getCurrencies(providers);
    }

    /**
     * Query all currencies matching the given query.
     *
     * @param query The {@link CurrencyQuery}, not null.
     * @return the list of known currencies, never null.
     */
    public static CurrencyUnit getCurrency(CurrencyQuery query) {
        if(monetaryCurrenciesSpi()==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi().getCurrency(query);
    }


    /**
     * Query all currencies matching the given query.
     *
     * @param query The {@link CurrencyQuery}, not null.
     * @return the list of known currencies, never null.
     */
    public static Collection<CurrencyUnit> getCurrencies(CurrencyQuery query) {
        if(monetaryCurrenciesSpi()==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi().getCurrencies(query);
    }

    /**
     * Query all currencies matching the given query.
     *
     * @return the list of known currencies, never null.
     */
    public static Set<String> getCurrencyProviderNames() {
        if(monetaryCurrenciesSpi()==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi().getProviderNames();
    }

    /**
     * Query the list and ordering of provider names modelling the default provider chain to be used, if no provider
     * chain was explicitly set..
     *
     * @return the orderend list provider names, modelling the default provider chain used, never null.
     */
    public static List<String> getDefaultCurrencyProviderChain() {
        if(monetaryCurrenciesSpi()==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi().getDefaultProviderChain();
    }

    /**
     * Access an {@link MonetaryAmountFactory} for the given {@link MonetaryAmount} implementation
     * type.
     *
     * @param amountType {@link MonetaryAmount} implementation type, nor {@code null}.
     * @return the corresponding {@link MonetaryAmountFactory}, never {@code null}.
     * @throws MonetaryException if no {@link MonetaryAmountFactory} targeting the given {@link MonetaryAmount}
     *                           implementation class is registered.
     */
    public static <T extends MonetaryAmount> MonetaryAmountFactory<T> getAmountFactory(Class<T> amountType) {
        MonetaryAmountsSingletonSpi spi = monetaryAmountsSingletonSpi();
        if(spi ==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonSpi loaded, query functionality is not available.");
        }
        MonetaryAmountFactory<T> factory = spi.getAmountFactory(amountType);
        if(factory==null){
            throw new MonetaryException("No AmountFactory available for type: " + amountType.getName());
        }
        return factory;
    }

    /**
     * Access the default {@link MonetaryAmountFactory} as defined by
     * {@link javax.money.spi.MonetaryAmountsSingletonSpi#getDefaultAmountFactory()}.
     *
     * @return the {@link MonetaryAmountFactory} corresponding to default amount type,
     * never {@code null}.
     * @throws MonetaryException if no {@link MonetaryAmountFactory} targeting the default amount type
     *                           implementation class is registered.
     */
    public static MonetaryAmountFactory<?> getDefaultAmountFactory() {
        MonetaryAmountsSingletonSpi spi = monetaryAmountsSingletonSpi();
        if(spi ==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonSpi loaded, query functionality is not available.");
        }
        return spi.getDefaultAmountFactory();
    }

    /**
     * Access all currently available {@link MonetaryAmount} implementation classes that are
     * accessible from this {@link MonetaryAmount} singleton.
     *
     * @return all currently available {@link MonetaryAmount} implementation classes that have
     * corresponding {@link MonetaryAmountFactory} instances provided, never {@code null}
     */
    public static Collection<MonetaryAmountFactory<?>> getAmountFactories() {
        if(monetaryAmountsSingletonSpi()==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonSpi loaded, query functionality is not available.");
        }
        return monetaryAmountsSingletonSpi().getAmountFactories();
    }

    /**
     * Access all currently available {@link MonetaryAmount} implementation classes that are
     * accessible from this {@link MonetaryAmount} singleton.
     *
     * @return all currently available {@link MonetaryAmount} implementation classes that have
     * corresponding {@link MonetaryAmountFactory} instances provided, never {@code null}
     */
    public static Collection<Class<? extends MonetaryAmount>> getAmountTypes() {
        if(monetaryAmountsSingletonSpi()==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonSpi loaded, query functionality is not available.");
        }
        return monetaryAmountsSingletonSpi().getAmountTypes();
    }

    /**
     * Access the default {@link MonetaryAmount} implementation class that is
     * accessible from this {@link MonetaryAmount} singleton.
     *
     * @return all current default {@link MonetaryAmount} implementation class, never {@code null}
     */
    public static Class<? extends MonetaryAmount> getDefaultAmountType() {
        if(monetaryAmountsSingletonSpi()==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonSpi loaded, query functionality is not available.");
        }
        return monetaryAmountsSingletonSpi().getDefaultAmountType();
    }

    /**
     * Executes the query and returns the factory found, if there is only one factory.
     * If multiple factories match the query, one is selected.
     *
     * @param query the factory query, not null.
     * @return the factory found, or null.
     */
    public static MonetaryAmountFactory getAmountFactory(MonetaryAmountFactoryQuery query) {
        if(monetaryAmountsSingletonQuerySpi()==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonQuerySpi loaded, query functionality is not available.");
        }
        return monetaryAmountsSingletonQuerySpi().getAmountFactory(query);
    }

    /**
     * Returns all factory instances that match the query.
     *
     * @param query the factory query, not null.
     * @return the instances found, never null.
     */
    public static Collection<MonetaryAmountFactory<?>> getAmountFactories(MonetaryAmountFactoryQuery query) {
        if(monetaryAmountsSingletonQuerySpi()==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonQuerySpi loaded, query functionality is not available.");
        }
        return monetaryAmountsSingletonQuerySpi().getAmountFactories(query);
    }

    /**
     * Allows to check if any of the <i>get</i>XXX methods return non empty/non null results of {@link MonetaryAmountFactory}.
     *
     * @param query the factory query, not null.
     * @return true, if at least one {@link MonetaryAmountFactory} matches the query.
     */
    public static boolean isAvailable(MonetaryAmountFactoryQuery query) {
        if(monetaryAmountsSingletonQuerySpi()==null){
            throw new MonetaryException(
                    "No MonetaryAmountsSingletonQuerySpi loaded, query functionality is not available.");
        }
        return monetaryAmountsSingletonQuerySpi().isAvailable(query);
    }

    /**
     * Creates a rounding that can be added as {@link MonetaryOperator} to
     * chained calculations. The instance will lookup the concrete
     * {@link MonetaryOperator} instance from
     * based on the input {@link MonetaryAmount}'s {@link CurrencyUnit}.
     *
     * @return the (shared) default rounding instance.
     */
    public static MonetaryRounding getDefaultRounding() {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().getDefaultRounding();
    }


    /**
     * Creates an {@link MonetaryOperator} for rounding {@link MonetaryAmount}
     * instances given a currency.
     *
     * @param currencyUnit The currency, which determines the required scale. As
     *                     {@link java.math.RoundingMode}, by default, {@link java.math.RoundingMode#HALF_UP}
     *                     is used.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return a new instance {@link MonetaryOperator} implementing the
     * rounding, never {@code null}.
     */
    public static MonetaryRounding getRounding(CurrencyUnit currencyUnit, String... providers) {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().getRounding(currencyUnit, providers);
    }

    /**
     * Access an {@link MonetaryOperator} for custom rounding
     * {@link MonetaryAmount} instances.
     *
     * @param roundingName The rounding identifier.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return the corresponding {@link MonetaryOperator} implementing the
     * rounding, never {@code null}.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link javax.money.spi.RoundingProviderSpi} instance.
     */
    public static MonetaryRounding getRounding(String roundingName, String... providers) {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().getRounding(roundingName, providers);
    }

    /**
     * Access a {@link MonetaryRounding} using a possibly complex query.
     *
     * @param roundingQuery The {@link RoundingQuery} that may contains arbitrary parameters to be
     *                      evaluated.
     * @return the corresponding {@link MonetaryRounding}, never {@code null}.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link javax.money.spi.RoundingProviderSpi} instance.
     */
    public static MonetaryRounding getRounding(RoundingQuery roundingQuery) {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().getRounding(roundingQuery);
    }

    /**
     * Checks if a {@link MonetaryRounding} is available given a roundingId.
     *
     * @param roundingName The rounding identifier.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return true, if a corresponding {@link MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link javax.money.spi.RoundingProviderSpi} instance.
     */
    public static boolean isRoundingAvailable(String roundingName, String... providers) {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().isRoundingAvailable(roundingName, providers);
    }

    /**
     * Checks if a {@link MonetaryRounding} is available given a roundingId.
     *
     * @param currencyUnit The currency, which determines the required scale. As {@link java.math.RoundingMode},
     *                     by default, {@link java.math.RoundingMode#HALF_UP} is used.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return true, if a corresponding {@link MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link javax.money.spi.RoundingProviderSpi} instance.
     */
    public static boolean isRoundingAvailable(CurrencyUnit currencyUnit, String... providers) {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().isRoundingAvailable(currencyUnit, providers);
    }

    /**
     * Checks if a {@link MonetaryRounding} matching the query is available.
     *
     * @param roundingQuery The {@link RoundingQuery} that may contains arbitrary parameters to be
     *                      evaluated.
     * @return true, if a corresponding {@link MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link javax.money.spi.RoundingProviderSpi} instance.
     */
    public static boolean isRoundingAvailable(RoundingQuery roundingQuery) {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().isRoundingAvailable(roundingQuery);
    }


    /**
     * Access multiple {@link MonetaryRounding} instances using a possibly complex query
     *
     * @param roundingQuery The {@link RoundingQuery} that may contains arbitrary parameters to be
     *                      evaluated.
     * @return all {@link MonetaryRounding} instances macthing the query, never {@code null}.
     */
    public static Collection<MonetaryRounding> getRoundings(RoundingQuery roundingQuery) {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().getRoundings(roundingQuery);
    }


    /**
     * Allows to access the names of the current defined roundings.
     *
     * @param providers the providers and ordering to be used. By default providers and ordering as defined in
     *                  #getDefaultProviders is used.
     * @return the set of custom rounding ids, never {@code null}.
     */
    public static Set<String> getRoundingNames(String... providers) {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().getRoundingNames(providers);
    }

    /**
     * Allows to access the names of the current registered providers.
     *
     * @return the set of provider names, never {@code null}.
     */
    public static Set<String> getRoundingProviderNames() {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().getProviderNames();
    }


    /**
     * Allows to access the default providers chain usef if no provider chain was passed explicitly..
     *
     * @return the chained list of provider names, never {@code null}.
     */
    public static List<String> getDefaultRoundingProviderChain() {
        if(monetaryRoundingsSpi()==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi().getDefaultProviderChain();
    }


}
