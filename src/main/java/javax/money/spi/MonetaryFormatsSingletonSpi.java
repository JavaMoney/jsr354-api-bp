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
package javax.money.spi;

import javax.money.MonetaryException;
import javax.money.format.AmountFormatQuery;
import javax.money.format.MonetaryAmountFormat;
import java.util.*;

/**
 * This interface models the singleton functionality of {@link javax.money.format.MonetaryFormats}.
 * <p>
 * Implementations of this interface must be thread-safe.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public interface MonetaryFormatsSingletonSpi {

    /**
     * Get all available locales. This equals to {@link MonetaryAmountFormatProviderSpi#getAvailableLocales()}.
     *
     * @param providers The (optional) providers to be used, oredered correspondingly.
     * @return all available locales, never {@code null}.
     */
    Set<Locale> getAvailableLocales(String... providers);

    /**
     * Access all {@link javax.money.format.MonetaryAmountFormat} instances matching the given {@link javax.money.format
     * .AmountFormatQuery}.
     *
     * @param formatQuery The format query defining the requirements of the formatters.
     * @return the corresponding {@link javax.money.format.MonetaryAmountFormat} instances, never null
     */
    Collection<MonetaryAmountFormat> getAmountFormats(AmountFormatQuery formatQuery);

    /**
     * Get the names of the currently registered format providers.
     *
     * @return the provider names, never null.
     */
    Set<String> getProviderNames();

    /**
     * Get the default provider chain, identified by the unique provider names in order as evaluated and used.
     *
     * @return the default provider chain, never null.
     */
    List<String> getDefaultProviderChain();

    /**
     * Access an {@link javax.money.format.MonetaryAmountFormat} given a {@link javax.money.format
     * .AmountFormatQuery}.
     *
     * @param formatQuery The format query defining the requirements of the formatter.
     * @return the corresponding {@link javax.money.format.MonetaryAmountFormat}
     * @throws javax.money.MonetaryException if no registered {@link javax.money.spi
     *                                       .MonetaryAmountFormatProviderSpi} can provide a
     *                                       corresponding {@link javax.money.format.MonetaryAmountFormat} instance.
     */
    MonetaryAmountFormat getAmountFormat(AmountFormatQuery formatQuery);

    /**
     * Checks if a {@link javax.money.format.MonetaryAmountFormat} is available given a {@link javax.money.format
     * .AmountFormatQuery}.
     *
     * @param formatQuery The format query defining the requirements of the formatter.
     * @return true, if a t least one {@link javax.money.format.MonetaryAmountFormat} is matching the query.
     */
    boolean isAvailable(AmountFormatQuery formatQuery);

    /**
     * Checks if a {@link javax.money.format.MonetaryAmountFormat} is available given a {@link javax.money.format
     * .AmountFormatQuery}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers The (optional) providers to be used, oredered correspondingly.
     * @return true, if a t least one {@link javax.money.format.MonetaryAmountFormat} is matching the query.
     */
    boolean isAvailable(Locale locale, String... providers);

    /**
     * Access the default {@link MonetaryAmountFormat} given a {@link java.util.Locale}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers The (optional) providers to be used, oredered correspondingly.
     * @return the matching {@link MonetaryAmountFormat}
     * @throws MonetaryException if no registered {@link MonetaryAmountFormatProviderSpi} can provide a
     *                           corresponding {@link MonetaryAmountFormat} instance.
     */
    MonetaryAmountFormat getAmountFormat(Locale locale, String... providers);

    /**
     * Access the default {@link MonetaryAmountFormat} given a {@link java.util.Locale}.
     *
     * @param formatName the target format name, not {@code null}.
     * @param providers  The (optional) providers to be used, oredered correspondingly.
     * @return the matching {@link MonetaryAmountFormat}
     * @throws MonetaryException if no registered {@link MonetaryAmountFormatProviderSpi} can provide a
     *                           corresponding {@link MonetaryAmountFormat} instance.
     */
    MonetaryAmountFormat getAmountFormat(String formatName, String... providers);
}
