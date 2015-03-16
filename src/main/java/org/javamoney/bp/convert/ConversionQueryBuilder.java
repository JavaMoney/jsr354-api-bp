/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2014, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.convert;

import org.javamoney.bp.AbstractQueryBuilder;
import org.javamoney.bp.CurrencyUnit;
import org.javamoney.bp.MonetaryCurrencies;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Builder class for creating new instances of {@link org.javamoney.bp.convert.ConversionQuery} adding detailed
 * information about a {@link org.javamoney.bp.convert.CurrencyConversion} instance.
 * <p>
 * Note this class is NOT thread-safe.
 *
 * @see org.javamoney.bp.convert.MonetaryConversions#getConversion(ConversionQuery)
 */
public final class ConversionQueryBuilder extends AbstractQueryBuilder<ConversionQueryBuilder, ConversionQuery> {

    private ConversionQueryBuilder() {
    }

    /**
     * Set the providers to be considered. If not set explicitly the <i>default</i> ISO currencies as
     * returned by {@link java.util.Currency} is used.
     *
     * @param rateTypes the rate types to use, not null.
     * @return the query for chaining.
     */
    public ConversionQueryBuilder setRateTypes(RateType... rateTypes) {
        return set("rateTypes", new HashSet<RateType>(Arrays.asList(rateTypes)));
    }

    /**
     * Set the providers to be considered. If not set explicitly the <i>default</i> ISO currencies as
     * returned by {@link java.util.Currency} is used.
     *
     * @param rateTypes the rate types to use, not null.
     * @return the query for chaining.
     */
    public ConversionQueryBuilder setRateTypes(Set<RateType> rateTypes) {
        return set(ConversionQuery.KEY_RATE_TYPES, rateTypes);
    }

    /**
     * Sets the base currency.
     *
     * @param currency the base currency
     * @return the query for chaining.
     */
    public ConversionQueryBuilder setBaseCurrency(CurrencyUnit currency) {
        return set(ConversionQuery.KEY_BASE_CURRENCY, currency);
    }

    /**
     * Sets the base currency.
     *
     * @param currencyCode the currency code, resolvable through {@link javax.money
     *                     .MonetaryCurrencies#getCurrency(String, String...)}, not null.
     * @return the query for chaining.
     */
    public ConversionQueryBuilder setBaseCurrency(String currencyCode) {
        return setBaseCurrency(MonetaryCurrencies.getCurrency(currencyCode));
    }

    /**
     * Sets the term currency.
     *
     * @param currency the base currency
     * @return the query for chaining.
     */
    public ConversionQueryBuilder setTermCurrency(CurrencyUnit currency) {
        return set(ConversionQuery.KEY_TERM_CURRENCY, currency);
    }

    /**
     * Sets the term currency.
     *
     * @param currencyCode the currency code, resolvable through {@link javax.money
     *                     .MonetaryCurrencies#getCurrency(String, String...)}, not null.
     * @return the query for chaining.
     */
    public ConversionQueryBuilder setTermCurrency(String currencyCode) {
        return setTermCurrency(MonetaryCurrencies.getCurrency(currencyCode));
    }

    /**
     * Creates a new instance of {@link ConversionQuery}.
     *
     * @return a new {@link ConversionQuery} instance.
     */
    @Override
    public ConversionQuery build() {
        return new ConversionQuery(this);
    }

    /**
     * Creates a new {@link org.javamoney.bp.convert.ConversionQueryBuilder} instance.
     *
     * @return a new {@link org.javamoney.bp.convert.ConversionQueryBuilder} instance, never null.
     */
    public static ConversionQueryBuilder of() {
        return new ConversionQueryBuilder();
    }

    /**
     * Creates a new {@link org.javamoney.bp.convert.ConversionQueryBuilder} instance.
     *
     * @param query the {@link org.javamoney.bp.convert.ConversionQuery} instance to be used as a template.
     * @return a new {@link org.javamoney.bp.convert.ConversionQueryBuilder} instance, never null.
     */
    public static ConversionQueryBuilder of(ConversionQuery query) {
        return new ConversionQueryBuilder().importContext(query);
    }

}
