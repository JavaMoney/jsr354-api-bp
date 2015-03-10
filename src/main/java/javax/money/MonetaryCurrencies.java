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
import javax.money.spi.CurrencyProviderSpi;
import javax.money.spi.MonetaryCurrenciesSingletonSpi;
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
public final class MonetaryCurrencies {
    /**
     * The used {@link javax.money.spi.MonetaryCurrenciesSingletonSpi} instance.
     */
    private static final MonetaryCurrenciesSingletonSpi monetaryCurrenciesSpi = loadMonetaryCurrenciesSingletonSpi();

    /**
     * Required for deserialization only.
     */
    private MonetaryCurrencies() {
    }

    /**
     * Loads the SPI backing bean.
     *
     * @return the {@link MonetaryCurrenciesSingletonSpi} backing bean to be used.
     */
    private static MonetaryCurrenciesSingletonSpi loadMonetaryCurrenciesSingletonSpi() {
        try {
            MonetaryCurrenciesSingletonSpi spi = Bootstrap
                    .getService(MonetaryCurrenciesSingletonSpi.class);
            if(spi==null) {
                spi = new DefaultMonetaryCurrenciesSingletonSpi();
            }
            return spi;
        } catch (Exception e) {
            Logger.getLogger(MonetaryCurrencies.class.getName())
                    .log(Level.INFO, "Failed to load MonetaryCurrenciesSingletonSpi, using default.", e);
            return new DefaultMonetaryCurrenciesSingletonSpi();
        }
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
        if(monetaryCurrenciesSpi==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi.getCurrency(currencyCode, providers);
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
        if(monetaryCurrenciesSpi==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi.getCurrency(locale, providers);
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
        if(monetaryCurrenciesSpi==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi.getCurrencies(locale, providers);
    }

    /**
     * Allows to check if a {@link CurrencyUnit} instance is defined, i.e.
     * accessible from {@link javax.money.MonetaryCurrencies#getCurrency(String, String...)}.
     *
     * @param code      the currency code, not {@code null}.
     * @param providers the (optional) specification of providers to consider.
     * @return {@code true} if {@link javax.money.MonetaryCurrencies#getCurrency(String, String...)}
     * would return a result for the given code.
     */
    public static boolean isCurrencyAvailable(String code, String... providers) {
        return Objects.nonNull(monetaryCurrenciesSpi) && monetaryCurrenciesSpi.isCurrencyAvailable(code, providers);
    }

    /**
     * Allows to check if a {@link javax.money.CurrencyUnit} instance is
     * defined, i.e. accessible from {@link #getCurrency(String, String...)}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers the (optional) specification of providers to consider.
     * @return {@code true} if {@link #getCurrencies(java.util.Locale, String...)} would return a
     * result containing a currency with the given code.
     */
    public static boolean isCurrencyAvailable(Locale locale, String... providers) {
        return Objects.nonNull(monetaryCurrenciesSpi) && monetaryCurrenciesSpi.isCurrencyAvailable(locale, providers);
    }

    /**
     * Access all currencies known.
     *
     * @param providers the (optional) specification of providers to consider.
     * @return the list of known currencies, never null.
     */
    public static Collection<CurrencyUnit> getCurrencies(String... providers) {
        if(monetaryCurrenciesSpi==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi.getCurrencies(providers);
    }

    /**
     * Query all currencies matching the given query.
     *
     * @param query The {@link javax.money.CurrencyQuery}, not null.
     * @return the list of known currencies, never null.
     */
    public static CurrencyUnit getCurrency(CurrencyQuery query) {
        if(monetaryCurrenciesSpi==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi.getCurrency(query);
    }


    /**
     * Query all currencies matching the given query.
     *
     * @param query The {@link javax.money.CurrencyQuery}, not null.
     * @return the list of known currencies, never null.
     */
    public static Collection<CurrencyUnit> getCurrencies(CurrencyQuery query) {
        if(monetaryCurrenciesSpi==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi.getCurrencies(query);
    }

    /**
     * Query all currencies matching the given query.
     *
     * @return the list of known currencies, never null.
     */
    public static Set<String> getProviderNames() {
        if(monetaryCurrenciesSpi==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi.getProviderNames();
    }

    /**
     * Query the list and ordering of provider names modelling the default provider chain to be used, if no provider
     * chain was explicitly set..
     *
     * @return the orderend list provider names, modelling the default provider chain used, never null.
     */
    public static List<String> getDefaultProviderChain() {
        if(monetaryCurrenciesSpi==null) {
            throw new MonetaryException("No MonetaryCurrenciesSingletonSpi loaded, check your system setup.");
        }
        return monetaryCurrenciesSpi.getDefaultProviderChain();
    }

    /**
     * Factory singleton for {@link javax.money.CurrencyUnit} instances as provided by the
     * different registered {@link javax.money.spi.CurrencyProviderSpi} instances.
     * <p>
     * This class is thread safe.
     *
     * @author Anatole Tresch
     * @version 0.8
     */
    private static final class DefaultMonetaryCurrenciesSingletonSpi implements MonetaryCurrenciesSingletonSpi {

        @Override
        public Set<CurrencyUnit> getCurrencies(CurrencyQuery query) {
            Set<CurrencyUnit> result = new HashSet<CurrencyUnit>();
            for (CurrencyProviderSpi spi : Bootstrap.getServices(CurrencyProviderSpi.class)) {
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

        /**
         * This default implementation simply returns all providers defined in arbitrary order.
         *
         * @return the default provider chain, never null.
         */
        @Override
        public List<String> getDefaultProviderChain() {
            List<String> list = new ArrayList<String>();
            list.addAll(getProviderNames());
            Collections.sort(list);
            return list;
        }

        /**
         * Get the names of the currently loaded providers.
         *
         * @return the names of the currently loaded providers, never null.
         */
        @Override
        public Set<String> getProviderNames() {
            Set<String> result = new HashSet<String>();
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
         * available as provided by {@link CurrencyProviderSpi} instances registered
         * with the {@link javax.money.spi.Bootstrap}.
         *
         * @param currencyCode the ISO currency code, not {@code null}.
         * @param providers    the (optional) specification of providers to consider. If not set (empty) the providers
         *                     as defined by #getDefaultProviderChain() should be used.
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
         * with the {@link javax.money.spi.Bootstrap}.
         *
         * @param country   the ISO currency's country, not {@code null}.
         * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
         *                  as defined by #getDefaultProviderChain() should be used.
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
         *                  as defined by #getDefaultProviderChain() should be used.
         * @return a collection of all known currencies, never null.
         */
        public Set<CurrencyUnit> getCurrencies(Locale locale, String... providers) {
            return getCurrencies(CurrencyQueryBuilder.of().setCountries(locale).setProviderNames(providers).build());
        }

        /**
         * Allows to check if a {@link javax.money.CurrencyUnit} instance is defined, i.e.
         * accessible from {@link javax.money.spi.BaseMonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}.
         *
         * @param code      the currency code, not {@code null}.
         * @param providers the (optional) specification of providers to consider. If not set (empty) the providers
         *                  as defined by #getDefaultProviderChain() should be used.
         * @return {@code true} if {@link javax.money.spi.BaseMonetaryCurrenciesSingletonSpi#getCurrency(String, String...)}
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
         *                  as defined by #getDefaultProviderChain() should be used.
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
         *                  as defined by #getDefaultProviderChain() should be used.
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

}
