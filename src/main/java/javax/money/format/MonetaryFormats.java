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
package javax.money.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.money.MonetaryException;
import javax.money.spi.Bootstrap;
import javax.money.spi.MonetaryAmountFormatProviderSpi;
import javax.money.spi.MonetaryFormatsSingletonSpi;

/**
 * This class models the singleton accessor for {@link MonetaryAmountFormat} instances.
 * <p>
 * This class is thread-safe.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public final class MonetaryFormats {

    private static MonetaryFormatsSingletonSpi monetaryFormatsSingletonSpi() {
        try {
            MonetaryFormatsSingletonSpi spi = Bootstrap.getService(MonetaryFormatsSingletonSpi.class);
            if(spi==null){
                spi = new DefaultMonetaryFormatsSingletonSpi();
            }
            return spi;
        } catch (Exception e) {
            Logger.getLogger(MonetaryFormats.class.getName())
                    .log(Level.WARNING, "Failed to load MonetaryFormatsSingletonSpi, using default.", e);
            return new DefaultMonetaryFormatsSingletonSpi();
        }
    }

    /**
     * Private singleton constructor.
     */
    private MonetaryFormats() {
        // Singleton
    }

    /**
     * Checks if a {@link MonetaryAmountFormat} is available for the given {@link java.util.Locale} and providers.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers The providers to be queried, if not set the providers as defined by #getDefaultRoundingProviderChain()
     *                  are queried.
     * @return true, if a corresponding {@link MonetaryAmountFormat} is accessible.
     */
    public static boolean isAvailable(Locale locale, String... providers) {
        if(monetaryFormatsSingletonSpi()==null){
            throw new MonetaryException(
                    "No MonetaryFormatsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return monetaryFormatsSingletonSpi().isAvailable(locale, providers);
    }

    /**
     * Access the default {@link MonetaryAmountFormat} given a {@link java.util.Locale}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers The providers to be queried, if not set the providers as defined by #getDefaultRoundingProviderChain()
     *                  are queried.
     * @return the matching {@link MonetaryAmountFormat}
     * @throws MonetaryException if no registered {@link javax.money.spi.MonetaryAmountFormatProviderSpi} can provide a
     *                           corresponding {@link MonetaryAmountFormat} instance.
     */
    public static MonetaryAmountFormat getAmountFormat(Locale locale, String... providers) {
        return getAmountFormat(AmountFormatQueryBuilder.of(locale).setProviderNames(providers).setLocale(locale).build());
    }

    /**
     * Checks if a {@link MonetaryAmountFormat} is available for the given {@link AmountFormatQuery}.
     *
     * @param formatQuery the required {@link AmountFormatQuery}, not {@code null}. If the query does not define
     *                    any explicit provider chain, the providers as defined by #getDefaultRoundingProviderChain()
     *                    are used.
     * @return true, if a corresponding {@link MonetaryAmountFormat} is accessible.
     */
    public static boolean isAvailable(AmountFormatQuery formatQuery) {
        if(monetaryFormatsSingletonSpi()==null){
            throw new MonetaryException(
                    "No MonetaryFormatsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return monetaryFormatsSingletonSpi().isAvailable(formatQuery);
    }

    /**
     * Access the default {@link MonetaryAmountFormat} given a {@link java.util.Locale}.
     *
     * @param formatQuery the required {@link AmountFormatQuery}, not {@code null}. If the query does not define
     *                    any explicit provider chain, the providers as defined by #getDefaultRoundingProviderChain()
     *                    are used.
     * @return the matching {@link MonetaryAmountFormat}
     * @throws MonetaryException if no registered {@link javax.money.spi.MonetaryAmountFormatProviderSpi} can provide a
     *                           corresponding {@link MonetaryAmountFormat} instance.
     */
    public static MonetaryAmountFormat getAmountFormat(AmountFormatQuery formatQuery) {
        if(monetaryFormatsSingletonSpi()==null){
            throw new MonetaryException(
                    "No MonetaryFormatsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return monetaryFormatsSingletonSpi().getAmountFormat(formatQuery);
    }

    /**
     * Access all {@link MonetaryAmountFormat} instances that match the given a {@link AmountFormatQuery}.
     *
     * @param formatQuery the required {@link AmountFormatQuery}, not {@code null}. If the query does not define
     *                    any explicit provider chain, the providers as defined by #getDefaultRoundingProviderChain()
     *                    are used.
     * @return the matching {@link MonetaryAmountFormat}
     * @throws MonetaryException if no registered {@link javax.money.spi.MonetaryAmountFormatProviderSpi} can provide a
     *                           corresponding {@link MonetaryAmountFormat} instance.
     */
    public static Collection<MonetaryAmountFormat> getAmountFormats(AmountFormatQuery formatQuery) {
        if(monetaryFormatsSingletonSpi()==null){
            throw new MonetaryException(
                    "No MonetaryFormatsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return monetaryFormatsSingletonSpi().getAmountFormats(formatQuery);
    }

    /**
     * Access the a {@link MonetaryAmountFormat} given its styleId.
     *
     * @param formatName the target format name, not {@code null}.
     * @param providers  The providers to be used, if not set the providers as defined by #getDefaultRoundingProviderChain() are
     *                   used.
     * @return the matching {@link MonetaryAmountFormat}
     * @throws MonetaryException if no registered {@link javax.money.spi.MonetaryAmountFormatProviderSpi} can provide a
     *                           corresponding {@link MonetaryAmountFormat} instance.
     */
    public static MonetaryAmountFormat getAmountFormat(String formatName, String... providers) {
        return getAmountFormat(AmountFormatQueryBuilder.of(formatName).setProviderNames(providers).build());
    }

    /**
     * Get all available locales. This equals to {@link javax.money.spi.MonetaryAmountFormatProviderSpi#getAvailableLocales()}.
     *
     * @param providers The providers to be used, if not set the providers as defined by #getDefaultRoundingProviderChain() are
     *                  used.
     * @return all available locales, never {@code null}.
     */
    public static Set<Locale> getAvailableLocales(String... providers) {
        return monetaryFormatsSingletonSpi().getAvailableLocales(providers);
    }

    /**
     * Get the names of the currently registered format providers.
     *
     * @return the provider names, never null.
     */
    public static Collection<String> getFormatProviderNames() {
        if(monetaryFormatsSingletonSpi()==null){
            throw new MonetaryException(
                    "No MonetaryFormatsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return monetaryFormatsSingletonSpi().getProviderNames();
    }

    /**
     * Get the default provider chain, identified by the unique provider names in order as evaluated and used.
     *
     * @return the default provider chain, never null.
     */
    public static List<String> getDefaultFormatProviderChain() {
        if(monetaryFormatsSingletonSpi()==null){
            throw new MonetaryException(
                    "No MonetaryFormatsSingletonSpi " + "loaded, query functionality is not available.");
        }
        return monetaryFormatsSingletonSpi().getDefaultProviderChain();
    }

    /**
     * This class models the singleton accessor for {@link MonetaryAmountFormat} instances.
     * <p>
     * This class is thread-safe.
     *
     * @author Anatole Tresch
     * @author Werner Keil
     */
    public static final class DefaultMonetaryFormatsSingletonSpi implements MonetaryFormatsSingletonSpi {


        /**
         * Access an {@link MonetaryAmountFormat} given a {@link javax.money.format
         * .AmountFormatContext}.
         *
         * @param formatQuery The format query defining the requirements of the formatter.
         * @return the corresponding {@link MonetaryAmountFormat}
         * @throws javax.money.MonetaryException if no registered {@link javax.money.spi
         *                                       .MonetaryAmountFormatProviderSpi} can provide a
         *                                       corresponding {@link MonetaryAmountFormat} instance.
         */
        public Collection<MonetaryAmountFormat> getAmountFormats(AmountFormatQuery formatQuery) {
            Collection<MonetaryAmountFormat> result = new ArrayList<>();
            for (MonetaryAmountFormatProviderSpi spi : Bootstrap.getServices(MonetaryAmountFormatProviderSpi.class)) {
                Collection<MonetaryAmountFormat> formats = spi.getAmountFormats(formatQuery);
                if (formats!=null) {
                    result.addAll(formats);
                }
            }
            return result;
        }

        @Override
        public Set<String> getProviderNames() {
            return getSpisAsMap().keySet();
        }

        /**
         * This default implementation simply returns all providers defined in arbitrary order.
         *
         * @return the default provider chain, never null.
         */
        @Override
        public List<String> getDefaultProviderChain() {
            List<String> list = new ArrayList<>();
            list.addAll(getProviderNames());
            Collections.sort(list);
            return list;
        }


        /**
         * Get all available locales. This equals to {@link javax.money.spi
         * .MonetaryAmountFormatProviderSpi#getAvailableLocales()}.
         *
         * @return all available locales, never {@code null}.
         */
        public Set<Locale> getAvailableLocales(String... providerNames) {
            Set<Locale> locales = new HashSet<>();
            Collection<MonetaryAmountFormatProviderSpi> spis = getSpis(providerNames);
            for (MonetaryAmountFormatProviderSpi spi : spis) {
                locales.addAll(spi.getAvailableLocales());
            }
            return locales;
        }

        private Map<String, MonetaryAmountFormatProviderSpi> getSpisAsMap() {
            Map<String, MonetaryAmountFormatProviderSpi> spis = new ConcurrentHashMap<>();
            for (MonetaryAmountFormatProviderSpi spi : Bootstrap.getServices(MonetaryAmountFormatProviderSpi.class)) {
                if (spi.getProviderName() == null) {
                    Logger.getLogger(MonetaryFormats.class.getName()).warning("MonetaryAmountFormatProviderSpi " +
                            "returns null for " +
                            "getProviderName: " +
                            spi.getClass().getName());
                }
                spis.put(spi.getProviderName(), spi);
            }
            return spis;
        }

        private Collection<MonetaryAmountFormatProviderSpi> getSpis(String... providerNames) {
            List<MonetaryAmountFormatProviderSpi> providers = new ArrayList<>();
            Map<String, MonetaryAmountFormatProviderSpi> spis = getSpisAsMap();
            if (providerNames.length == 0) {
                providers.addAll(spis.values());
            } else {
                for (String provName : providerNames) {
                    MonetaryAmountFormatProviderSpi spi = spis.get(provName);
                    if (spi==null) {
                        throw new IllegalArgumentException("MonetaryAmountFormatProviderSpi not found: " + provName);
                    }
                    providers.add(spi);
                }
            }
            return providers;
        }

        /**
         * Access an {@link MonetaryAmountFormat} given a {@link javax.money.format
         * .AmountFormatQuery}.
         *
         * @param formatQuery The format query defining the requirements of the formatter.
         * @return the corresponding {@link MonetaryAmountFormat}
         * @throws javax.money.MonetaryException if no registered {@link javax.money.spi
         *                                       .MonetaryAmountFormatProviderSpi} can provide a
         *                                       corresponding {@link MonetaryAmountFormat} instance.
         */
        public MonetaryAmountFormat getAmountFormat(AmountFormatQuery formatQuery) {
            Collection<MonetaryAmountFormat> formats = getAmountFormats(formatQuery);
            if (formats.isEmpty()) {
                throw new MonetaryException("No MonetaryAmountFormat for AmountFormatQuery " + formatQuery);
            }
            return formats.iterator().next();
        }

        /**
         * Checks if a {@link MonetaryAmountFormat} is available given a {@link javax.money.format
         * .AmountFormatQuery}.
         *
         * @param formatQuery The format query defining the requirements of the formatter.
         * @return true, if a t least one {@link MonetaryAmountFormat} is matching the query.
         */
        public boolean isAvailable(AmountFormatQuery formatQuery) {
            return !getAmountFormats(formatQuery).isEmpty();
        }

        /**
         * Checks if a {@link MonetaryAmountFormat} is available given a {@link javax.money.format
         * .AmountFormatQuery}.
         *
         * @param locale    the target {@link java.util.Locale}, not {@code null}.
         * @param providers The (optional) providers to be used, oredered correspondingly.
         * @return true, if a t least one {@link MonetaryAmountFormat} is matching the query.
         */
        public boolean isAvailable(Locale locale, String... providers) {
            return isAvailable(AmountFormatQuery.of(locale, providers));
        }

        /**
         * Access the default {@link MonetaryAmountFormat} given a {@link java.util.Locale}.
         *
         * @param locale    the target {@link java.util.Locale}, not {@code null}.
         * @param providers The (optional) providers to be used, oredered correspondingly.
         * @return the matching {@link MonetaryAmountFormat}
         * @throws javax.money.MonetaryException if no registered {@link javax.money.spi.MonetaryAmountFormatProviderSpi} can provide a
         *                           corresponding {@link MonetaryAmountFormat} instance.
         */
        public MonetaryAmountFormat getAmountFormat(Locale locale, String... providers) {
            return getAmountFormat(AmountFormatQueryBuilder.of(locale).setProviderNames(providers).build());
        }

        /**
         * Access the default {@link MonetaryAmountFormat} given a {@link java.util.Locale}.
         *
         * @param formatName the target format name, not {@code null}.
         * @param providers  The (optional) providers to be used, oredered correspondingly.
         * @return the matching {@link MonetaryAmountFormat}
         * @throws javax.money.MonetaryException if no registered {@link javax.money.spi.MonetaryAmountFormatProviderSpi} can provide a
         *                           corresponding {@link MonetaryAmountFormat} instance.
         */
        public MonetaryAmountFormat getAmountFormat(String formatName, String... providers) {
            return getAmountFormat(AmountFormatQueryBuilder.of(formatName).setProviderNames(providers).build());
        }
    }
}
