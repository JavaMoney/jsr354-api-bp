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

import javax.money.CurrencyUnit;
import javax.money.MonetaryException;
import javax.money.MonetaryRounding;
import javax.money.RoundingQuery;
import javax.money.RoundingQueryBuilder;

import java.util.Collection;

/**
 * This class models the accessor for rounding instances, modeled as
 * {@link javax.money.MonetaryOperator}.
 * <p>
 * This class is thread-safe.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public abstract class BaseMonetaryRoundingsSingletonSpi implements MonetaryRoundingsSingletonSpi {

    /**
     * Access a {@link javax.money.MonetaryRounding} for rounding {@link javax.money.MonetaryAmount}
     * instances given a currency.
     *
     * @param currencyUnit The currency, which determines the required precision. As
     *                     {@link java.math.RoundingMode}, by default, {@link java.math.RoundingMode#HALF_UP}
     *                     is sued.
     * @param providers    the optional provider list and ordering to be used
     * @return a new instance {@link javax.money.MonetaryOperator} implementing the
     * rounding, never {@code null}.
     * @throws javax.money.MonetaryException if no such rounding could be provided.
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
     * Access a {@link javax.money.MonetaryRounding} using the rounding name.
     *
     * @param roundingName The rounding name, not null.
     * @param providers    the optional provider list and ordering to be used
     * @return the corresponding {@link javax.money.MonetaryOperator} implementing the
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
     * javax.money.RoundingQuery#getProviderNames()}.
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
     * Checks if any {@link javax.money.MonetaryRounding} is matching the given query.
     *
     * @param query the rounding query, not null.
     * @return true, if at least one rounding matches the query.
     */
    public boolean isRoundingAvailable(RoundingQuery query) {
        return !getRoundings(query).isEmpty();
    }

    /**
     * Checks if a {@link javax.money.MonetaryRounding} is available given a roundingId.
     *
     * @param roundingId The rounding identifier.
     * @param providers  the providers and ordering to be used. By default providers and ordering as defined in
     *                   #getDefaultProviders is used.
     * @return true, if a corresponding {@link javax.money.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    public boolean isRoundingAvailable(String roundingId, String... providers) {
        return isRoundingAvailable(
                RoundingQueryBuilder.of().setProviderNames(providers).setRoundingName(roundingId).build());
    }

    /**
     * Checks if a {@link javax.money.MonetaryRounding} is available given a {@link javax.money.CurrencyUnit}.
     *
     * @param currencyUnit The currency, which determines the required precision. As {@link java.math.RoundingMode},
     *                     by default, {@link java.math.RoundingMode#HALF_UP} is used.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return true, if a corresponding {@link javax.money.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    public boolean isRoundingAvailable(CurrencyUnit currencyUnit, String... providers) {
        return isRoundingAvailable(RoundingQueryBuilder.of().setProviderNames(providers).setCurrency(currencyUnit).build());
    }
}
