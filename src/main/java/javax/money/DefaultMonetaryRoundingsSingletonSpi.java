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
package javax.money;

import javax.money.spi.Bootstrap;
import javax.money.spi.MonetaryRoundingsSingletonSpi;
import javax.money.spi.RoundingProviderSpi;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class models the accessor for rounding instances, modeled as
 * {@link MonetaryOperator}.
 * <p>
 * This class is thread-safe.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
final class DefaultMonetaryRoundingsSingletonSpi implements MonetaryRoundingsSingletonSpi {

    /**
     * An adaptive rounding instance that transparently looks up the correct
     * rounding.
     */
    private static final MonetaryRounding DEFAULT_ROUNDING = new DefaultCurrencyRounding();

        /**
         * Creates an rounding instance using {@link java.math.RoundingMode#UP} rounding.
         *
         * @return the corresponding {@link MonetaryOperator} implementing the
         * rounding.
         * @throws MonetaryException if no such rounding could be evaluated.
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
         * Access a {@link MonetaryRounding} for rounding {@link MonetaryAmount}
         * instances given a currency.
         *
         * @param currencyUnit The currency, which determines the required precision. As
         *                     {@link java.math.RoundingMode}, by default, {@link java.math.RoundingMode#HALF_UP}
         *                     is sued.
         * @param providers    the optional provider list and ordering to be used
         * @return a new instance {@link MonetaryOperator} implementing the
         * rounding, never {@code null}.
         * @throws MonetaryException if no such rounding could be provided.
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
         * Access a {@link MonetaryRounding} using the rounding name.
         *
         * @param roundingName The rounding name, not null.
         * @param providers    the optional provider list and ordering to be used
         * @return the corresponding {@link MonetaryOperator} implementing the
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
         * Checks if any {@link MonetaryRounding} is matching the given query.
         *
         * @param query the rounding query, not null.
         * @return true, if at least one rounding matches the query.
         */
        public boolean isRoundingAvailable(RoundingQuery query) {
            return !getRoundings(query).isEmpty();
        }

        /**
         * Checks if a {@link MonetaryRounding} is available given a roundingId.
         *
         * @param roundingId The rounding identifier.
         * @param providers  the providers and ordering to be used. By default providers and ordering as defined in
         *                   #getDefaultProviders is used.
         * @return true, if a corresponding {@link MonetaryRounding} is available.
         * @throws IllegalArgumentException if no such rounding is registered using a
         *                                  {@link javax.money.spi.RoundingProviderSpi} instance.
         */
        public boolean isRoundingAvailable(String roundingId, String... providers) {
            return isRoundingAvailable(
                    RoundingQueryBuilder.of().setProviderNames(providers).setRoundingName(roundingId).build());
        }

        /**
         * Checks if a {@link MonetaryRounding} is available given a {@link CurrencyUnit}.
         *
         * @param currencyUnit The currency, which determines the required precision. As {@link java.math.RoundingMode},
         *                     by default, {@link java.math.RoundingMode#HALF_UP} is used.
         * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
         *                     #getDefaultProviders is used.
         * @return true, if a corresponding {@link MonetaryRounding} is available.
         * @throws IllegalArgumentException if no such rounding is registered using a
         *                                  {@link javax.money.spi.RoundingProviderSpi} instance.
         */
        public boolean isRoundingAvailable(CurrencyUnit currencyUnit, String... providers) {
            return isRoundingAvailable(RoundingQueryBuilder.of().setProviderNames(providers).setCurrency(currencyUnit).build());
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
            MonetaryRounding r = Monetary.getRounding(amount.getCurrency());
            return r.apply(amount);
        }

        @Override
        public RoundingContext getRoundingContext() {
            return ROUNDING_CONTEXT;
        }
    }


}