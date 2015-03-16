/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency
 * API ("Specification") Copyright (c) 2012-2013, Credit Suisse All rights
 * reserved.
 */
package org.javamoney.bp;

import org.javamoney.bp.spi.Bootstrap;
import org.javamoney.bp.spi.MonetaryRoundingsSingletonSpi;
import org.javamoney.bp.spi.RoundingProviderSpi;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class models the accessor for rounding instances, modeled as
 * {@link org.javamoney.bp.MonetaryOperator}.
 * <p>
 * This class is thread-safe.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public final class MonetaryRoundings {

    /**
     * An adaptive rounding instance that transparently looks up the correct
     * rounding.
     */
    private static final MonetaryRounding DEFAULT_ROUNDING = new DefaultCurrencyRounding();

    /**
     * The used {@link org.javamoney.bp.spi.MonetaryCurrenciesSingletonSpi} instance.
     */
    private static final MonetaryRoundingsSingletonSpi monetaryRoundingsSpi = loadMonetaryRoundingsSingletonSpi();

    /**
     * Private singleton constructor.
     */
    private MonetaryRoundings() {
        // Singleton
    }

    /**
     * Loads the SPI backing bean.
     *
     * @return an instance of MonetaryRoundingsSingletonSpi.
     */
    private static MonetaryRoundingsSingletonSpi loadMonetaryRoundingsSingletonSpi() {
        try {
            MonetaryRoundingsSingletonSpi spi = Bootstrap
                    .getService(MonetaryRoundingsSingletonSpi.class);
            if(spi==null) {
                spi = new DefaultMonetaryRoundingsSingletonSpi();
            }
            return spi;
        } catch (Exception e) {
            Logger.getLogger(MonetaryCurrencies.class.getName())
                    .log(Level.SEVERE, "Failed to load MonetaryCurrenciesSingletonSpi, using default.", e);
            return new DefaultMonetaryRoundingsSingletonSpi();
        }
    }

    /**
     * Creates a rounding that can be added as {@link MonetaryOperator} to
     * chained calculations. The instance will lookup the concrete
     * {@link MonetaryOperator} instance from the {@link org.javamoney.bp.MonetaryRoundings}
     * based on the input {@link MonetaryAmount}'s {@link CurrencyUnit}.
     *
     * @return the (shared) default rounding instance.
     */
    public static MonetaryRounding getDefaultRounding() {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.getDefaultRounding();
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
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.getRounding(currencyUnit, providers);
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
     *                                  {@link RoundingProviderSpi} instance.
     */
    public static MonetaryRounding getRounding(String roundingName, String... providers) {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.getRounding(roundingName, providers);
    }

    /**
     * Access a {@link MonetaryRounding} using a possibly complex query.
     *
     * @param roundingQuery The {@link RoundingQuery} that may contains arbitrary parameters to be
     *                      evaluated.
     * @return the corresponding {@link org.javamoney.bp.MonetaryRounding}, never {@code null}.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    public static MonetaryRounding getRounding(RoundingQuery roundingQuery) {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.getRounding(roundingQuery);
    }

    /**
     * Checks if a {@link MonetaryRounding} is available given a roundingId.
     *
     * @param roundingName The rounding identifier.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return true, if a corresponding {@link org.javamoney.bp.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    public static boolean isRoundingAvailable(String roundingName, String... providers) {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.isRoundingAvailable(roundingName, providers);
    }

    /**
     * Checks if a {@link MonetaryRounding} is available given a roundingId.
     *
     * @param currencyUnit The currency, which determines the required scale. As {@link java.math.RoundingMode},
     *                     by default, {@link java.math.RoundingMode#HALF_UP} is used.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return true, if a corresponding {@link org.javamoney.bp.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    public static boolean isRoundingAvailable(CurrencyUnit currencyUnit, String... providers) {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.isRoundingAvailable(currencyUnit, providers);
    }

    /**
     * Checks if a {@link MonetaryRounding} matching the query is available.
     *
     * @param roundingQuery The {@link RoundingQuery} that may contains arbitrary parameters to be
     *                      evaluated.
     * @return true, if a corresponding {@link org.javamoney.bp.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    public static boolean isRoundingAvailable(RoundingQuery roundingQuery) {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.isRoundingAvailable(roundingQuery);
    }


    /**
     * Access multiple {@link MonetaryRounding} instances using a possibly complex query
     *
     * @param roundingQuery The {@link RoundingQuery} that may contains arbitrary parameters to be
     *                      evaluated.
     * @return all {@link org.javamoney.bp.MonetaryRounding} instances macthing the query, never {@code null}.
     */
    public static Collection<MonetaryRounding> getRoundings(RoundingQuery roundingQuery) {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.getRoundings(roundingQuery);
    }


    /**
     * Allows to access the names of the current defined roundings.
     *
     * @param providers the providers and ordering to be used. By default providers and ordering as defined in
     *                  #getDefaultProviders is used.
     * @return the set of custom rounding ids, never {@code null}.
     */
    public static Set<String> getRoundingNames(String... providers) {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.getRoundingNames(providers);
    }

    /**
     * Allows to access the names of the current registered providers.
     *
     * @return the set of provider names, never {@code null}.
     */
    public static Set<String> getProviderNames() {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.getProviderNames();
    }


    /**
     * Allows to access the default providers chain usef if no provider chain was passed explicitly..
     *
     * @return the chained list of provider names, never {@code null}.
     */
    public static List<String> getDefaultProviderChain() {
        if(monetaryRoundingsSpi==null){
            throw new MonetaryException("No MonetaryRoundingsSpi loaded, query functionality is not available.");
        }
        return monetaryRoundingsSpi.getDefaultProviderChain();
    }

    /**
     * Default Rounding that rounds a {@link MonetaryAmount} based on the
     * amount's {@link CurrencyUnit}.
     *
     * @author Anatole Tresch
     */
    private static final class DefaultCurrencyRounding implements MonetaryRounding, Serializable {

        private static final RoundingContext ROUNDING_CONTEXT = RoundingContextBuilder.of("default", "default").build();

        @Override
        public MonetaryAmount apply(MonetaryAmount amount) {
            MonetaryRounding r = getRounding(amount.getCurrency());
            return r.apply(amount);
        }

        @Override
        public RoundingContext getRoundingContext() {
            return ROUNDING_CONTEXT;
        }
    }

    /**
     * This class models the accessor for rounding instances, modeled as
     * {@link MonetaryOperator}.
     * <p>
     * This class is thread-safe.
     *
     * @author Anatole Tresch
     * @author Werner Keil
     */
    private static final class DefaultMonetaryRoundingsSingletonSpi implements MonetaryRoundingsSingletonSpi {

        /**
         * Creates an rounding instance using {@link java.math.RoundingMode#UP} rounding.
         *
         * @return the corresponding {@link org.javamoney.bp.MonetaryOperator} implementing the
         * rounding.
         * @throws org.javamoney.bp.MonetaryException if no such rounding could be evaluated.
         */
        public RoundingQueryBuilder createRoundingQueryBuilder() {
            throw new IllegalStateException("No MonetaryRoundingsSingletonSpi registered.");
        }

        /**
         * Get the default rounding, which deleggates rounding dynamically dependenging on the current  currency.
         *
         * @return the default rounding, never null.
         */
        public MonetaryRounding getDefaultRounding() {
            return DEFAULT_ROUNDING;
        }


        /**
         * Query all roundings matching the given {@link RoundingQuery}.
         *
         * @param query the rounding query, not null.
         * @return the collection found, not null.
         */
        @Override
        public Collection<MonetaryRounding> getRoundings(RoundingQuery query) {
            List<MonetaryRounding> result = new ArrayList<MonetaryRounding>();
            Collection<String> providerNames = query.getProviderNames();
            if (providerNames == null || providerNames.isEmpty()) {
                providerNames = getDefaultProviderChain();
            }
            Collection<RoundingProviderSpi> allProviders = Bootstrap.getServices(RoundingProviderSpi.class);
            for (String providerName : providerNames) {
                for(RoundingProviderSpi curProv: allProviders) {
                    if (providerName.equals(curProv.getProviderName())) {
                        try {
                            MonetaryRounding r = curProv.getRounding(query);
                            if (r != null) {
                                result.add(r);
                            }
                        } catch (Exception e) {
                            Logger.getLogger(DefaultMonetaryRoundingsSingletonSpi.class.getName())
                                    .log(Level.SEVERE, "Error loading RoundingProviderSpi from provider: " + curProv, e);
                        }
                    }
                }
            }
            return result;
        }


        /**
         * Get the names of all current registered providers.
         *
         * @return the names of all current registered providers, never null.
         */
        @Override
        public Set<String> getProviderNames() {
            Set<String> result = new HashSet<String>();
            for (RoundingProviderSpi prov : Bootstrap.getServices(RoundingProviderSpi.class)) {
                try {
                    result.add(prov.getProviderName());
                } catch (Exception e) {
                    Logger.getLogger(DefaultMonetaryRoundingsSingletonSpi.class.getName())
                            .log(Level.SEVERE, "Error loading RoundingProviderSpi from provider: " + prov, e);
                }
            }
            return result;
        }

        /**
         * Get the default providers list to be used.
         *
         * @return the default provider list and ordering, not null.
         */
        @Override
        public List<String> getDefaultProviderChain() {
            List<String> result = new ArrayList<String>();
            result.addAll(getProviderNames());
            Collections.sort(result);
            return result;
        }

        /**
         * Allows to access the identifiers of the current defined roundings.
         *
         * @param providers the providers and ordering to be used. By default providers and ordering as defined in
         *                  #getDefaultProviders is used, not null.
         * @return the set of custom rounding ids, never {@code null}.
         */
        public Set<String> getRoundingNames(String... providers) {
            Set<String> result = new HashSet<String>();
            String[] providerNames = providers;
            if (providerNames.length == 0) {
                providerNames = getDefaultProviderChain().toArray(new String[getDefaultProviderChain().size()]);
            }
            for (String providerName : providerNames) {
                for (RoundingProviderSpi prov : Bootstrap.getServices(RoundingProviderSpi.class)) {
                    try {
                        if (prov.getProviderName().equals(providerName) || prov.getProviderName().matches(providerName)) {
                            result.addAll(prov.getRoundingNames());
                        }
                    } catch (Exception e) {
                        Logger.getLogger(DefaultMonetaryRoundingsSingletonSpi.class.getName())
                                .log(Level.SEVERE, "Error loading RoundingProviderSpi from provider: " + prov, e);
                    }
                }
            }
            return result;
        }

        /**
         * Access a {@link org.javamoney.bp.MonetaryRounding} for rounding {@link org.javamoney.bp.MonetaryAmount}
         * instances given a currency.
         *
         * @param currencyUnit The currency, which determines the required precision. As
         *                     {@link java.math.RoundingMode}, by default, {@link java.math.RoundingMode#HALF_UP}
         *                     is sued.
         * @param providers    the optional provider list and ordering to be used
         * @return a new instance {@link org.javamoney.bp.MonetaryOperator} implementing the
         * rounding, never {@code null}.
         * @throws org.javamoney.bp.MonetaryException if no such rounding could be provided.
         */
        public MonetaryRounding getRounding(CurrencyUnit currencyUnit, String... providers) {
            MonetaryRounding op =
                    getRounding(RoundingQueryBuilder.of().setProviderNames(providers).setCurrency(currencyUnit).build());
            if(op==null) {
                throw new MonetaryException(
                        "No rounding provided for CurrencyUnit: " + currencyUnit.getCurrencyCode());
            }
            return op;
        }


        /**
         * Access a {@link org.javamoney.bp.MonetaryRounding} using the rounding name.
         *
         * @param roundingName The rounding name, not null.
         * @param providers    the optional provider list and ordering to be used
         * @return the corresponding {@link org.javamoney.bp.MonetaryOperator} implementing the
         * rounding, never {@code null}.
         * @throws IllegalArgumentException if no such rounding is registered using a
         *                                  {@link RoundingProviderSpi} instance.
         */
        public MonetaryRounding getRounding(String roundingName, String... providers) {
            MonetaryRounding op =
                    getRounding(RoundingQueryBuilder.of().setProviderNames(providers).setRoundingName(roundingName).build());
            if(op==null) {
                throw new MonetaryException("No rounding provided with rounding name: " + roundingName);
            }
            return op;
        }


        /**
         * Query a specific rounding with the given query. If multiple roundings match the query the first one is
         * selected, since the query allows to determine the providers and their ordering by setting {@link
         * RoundingQuery#getProviderNames()}.
         *
         * @param query the rounding query, not null.
         * @return the rounding found, or null, if no rounding matches the query.
         */
        public MonetaryRounding getRounding(RoundingQuery query) {
            Collection<MonetaryRounding> roundings = getRoundings(query);
            if (roundings.isEmpty()) {
                return null;
            }
            return roundings.iterator().next();
        }

        /**
         * Checks if any {@link org.javamoney.bp.MonetaryRounding} is matching the given query.
         *
         * @param query the rounding query, not null.
         * @return true, if at least one rounding matches the query.
         */
        public boolean isRoundingAvailable(RoundingQuery query) {
            return !getRoundings(query).isEmpty();
        }

        /**
         * Checks if a {@link org.javamoney.bp.MonetaryRounding} is available given a roundingId.
         *
         * @param roundingId The rounding identifier.
         * @param providers  the providers and ordering to be used. By default providers and ordering as defined in
         *                   #getDefaultProviders is used.
         * @return true, if a corresponding {@link org.javamoney.bp.MonetaryRounding} is available.
         * @throws IllegalArgumentException if no such rounding is registered using a
         *                                  {@link org.javamoney.bp.spi.RoundingProviderSpi} instance.
         */
        public boolean isRoundingAvailable(String roundingId, String... providers) {
            return isRoundingAvailable(
                    RoundingQueryBuilder.of().setProviderNames(providers).setRoundingName(roundingId).build());
        }

        /**
         * Checks if a {@link org.javamoney.bp.MonetaryRounding} is available given a {@link org.javamoney.bp.CurrencyUnit}.
         *
         * @param currencyUnit The currency, which determines the required precision. As {@link java.math.RoundingMode},
         *                     by default, {@link java.math.RoundingMode#HALF_UP} is used.
         * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
         *                     #getDefaultProviders is used.
         * @return true, if a corresponding {@link org.javamoney.bp.MonetaryRounding} is available.
         * @throws IllegalArgumentException if no such rounding is registered using a
         *                                  {@link org.javamoney.bp.spi.RoundingProviderSpi} instance.
         */
        public boolean isRoundingAvailable(CurrencyUnit currencyUnit, String... providers) {
            return isRoundingAvailable(RoundingQueryBuilder.of().setProviderNames(providers).setCurrency(currencyUnit).build());
        }

    }

}