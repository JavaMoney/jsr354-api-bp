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
package javax.money.spi;

import javax.money.MonetaryException;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import java.util.Collection;
import java.util.Locale;

/**
 * This interface models the singleton functionality of {@link javax.money.format.MonetaryFormats}.
 * <p>
 * Implementations of this interface must be thread-safe.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public abstract class BaseMonetaryFormatsSingletonSpi implements MonetaryFormatsSingletonSpi{

    /**
     * Access an {@link javax.money.format.MonetaryAmountFormat} given a {@link javax.money.spi
     * .AmountFormatQuery}.
     *
     * @param formatQuery The format query defining the requirements of the formatter.
     * @return the corresponding {@link javax.money.format.MonetaryAmountFormat}
     * @throws javax.money.MonetaryException if no registered {@link javax.money.spi
     *                                       .MonetaryAmountFormatProviderSpi} can provide a
     *                                       corresponding {@link javax.money.format.MonetaryAmountFormat} instance.
     */
    public MonetaryAmountFormat getAmountFormat(AmountFormatQuery formatQuery) {
        Collection<MonetaryAmountFormat> formats = getAmountFormats(formatQuery);
        if (formats.isEmpty()) {
            throw new MonetaryException("No MonetaryAmountFormat for AmountFormatQuery " + formatQuery);
        }
        return formats.iterator().next();
    }

    /**
     * Checks if a {@link javax.money.format.MonetaryAmountFormat} is available given a {@link javax.money.spi
     * .AmountFormatQuery}.
     *
     * @param formatQuery The format query defining the requirements of the formatter.
     * @return true, if a t least one {@link javax.money.format.MonetaryAmountFormat} is matching the query.
     */
    public boolean isAvailable(AmountFormatQuery formatQuery) {
        return !getAmountFormats(formatQuery).isEmpty();
    }

    /**
     * Checks if a {@link javax.money.format.MonetaryAmountFormat} is available given a {@link javax.money.spi
     * .AmountFormatQuery}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers The (optional) providers to be used, ordered correspondingly.
     * @return true, if a t least one {@link javax.money.format.MonetaryAmountFormat} is matching the query.
     */
    public boolean isAvailable(Locale locale, String... providers) {
        return isAvailable(AmountFormatQuery.of(locale, providers));
    }

    /**
     * Access the default {@link javax.money.format.MonetaryAmountFormat} given a {@link java.util.Locale}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers The (optional) providers to be used, ordered correspondingly.
     * @return the matching {@link javax.money.format.MonetaryAmountFormat}
     * @throws javax.money.MonetaryException if no registered {@link MonetaryAmountFormatProviderSpi} can provide a
     *                           corresponding {@link javax.money.format.MonetaryAmountFormat} instance.
     */
    public MonetaryAmountFormat getAmountFormat(Locale locale, String... providers) {
        return getAmountFormat(AmountFormatQueryBuilder.of(locale).setProviderNames(providers).build());
    }

    /**
     * Access the default {@link javax.money.format.MonetaryAmountFormat} given a {@link java.util.Locale}.
     *
     * @param formatName the target format name, not {@code null}.
     * @param providers  The (optional) providers to be used, ordered correspondingly.
     * @return the matching {@link javax.money.format.MonetaryAmountFormat}
     * @throws javax.money.MonetaryException if no registered {@link MonetaryAmountFormatProviderSpi} can provide a
     *                           corresponding {@link javax.money.format.MonetaryAmountFormat} instance.
     */
    public MonetaryAmountFormat getAmountFormat(String formatName, String... providers) {
        return getAmountFormat(AmountFormatQueryBuilder.of(formatName).setProviderNames(providers).build());
    }

}
