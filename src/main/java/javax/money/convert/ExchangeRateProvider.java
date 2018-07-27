/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency
 * API ("Specification") Copyright (c) 2012-2014, Credit Suisse All rights
 * reserved.
 */
package javax.money.convert;

import javax.money.CurrencyUnit;

/**
 * This interface defines access to the exchange rates provided by a provider.
 * The provider and its capabilities can be defined in arbitrary detail by the
 * corresponding {@link ProviderContext}.
 * Instances of this class must only provide conversion data for exact one provider, identified by
 * {@link ProviderContext#getProviderName()}.
 *
 * When accessing ExchangeRateProvider instances or {@link CurrencyConversion} instances from the
 * {@link MonetaryConversions}
 * in many cases a chain of providers will be returned. It is the reponsibility of the implementation code assembling
 * the chain to
 * establish a well defined coordination mechanism for evaluating the correct result. By default the first provider
 * in the chain that returns a non null result determines the final result of a call. Nevertheless adapting the
 * {@link javax.money.spi.MonetaryConversionsSingletonSpi} allows
 * to implement also alternate strategies, e.g. honoring different priorities of providers as well.
 * <p>
 * Implementations of this interface are required to be thread save.
 * <p>
 * Implementations of this class must neither be immutable nor serializable.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public interface ExchangeRateProvider{

    /**
     * Access the {@link ConversionContext} for this ExchangeRateProvider. Each instance of
     * ExchangeRateProvider provides conversion data for exact one
     * {@link ConversionContext} .
     *
     * @return the exchange rate type, never {@code null}.
     */
    ProviderContext getContext();

    /**
     * Access a {@link ExchangeRate} using the given currencies. The
     * {@link ExchangeRate} may be, depending on the data provider, real-time or
     * deferred. This method should return the rate that is <i>currently</i>
     * valid.
     *
     * @param conversionQuery the required {@link ConversionQuery}, not {@code null}
     * @return the matching {@link ExchangeRate}.
     * @throws CurrencyConversionException If no such rate is available.
     * @throws javax.money.MonetaryException           if one of the currency codes passed is not valid.
     * @see ConversionQueryBuilder
     */
    ExchangeRate getExchangeRate(ConversionQuery conversionQuery);

    /**
     * Access a {@link CurrencyConversion} that can be applied as a
     * {@link javax.money.MonetaryOperator} to an amount.
     *
     * @param conversionQuery the required {@link ConversionQuery}, not {@code null}
     * @return a new instance of a corresponding {@link CurrencyConversion},
     * never {@code null}.
     * @throws javax.money.MonetaryException if one of the currency codes passed is not valid.
     * @see ConversionQueryBuilder
     */
    CurrencyConversion getCurrencyConversion(ConversionQuery conversionQuery);


    /**
     * Checks if an {@link ExchangeRate} between two {@link javax.money.CurrencyUnit} is
     * available from this provider. This method should check, if a given rate
     * is <i>currently</i> defined.
     *
     * @param conversionQuery the required {@link ConversionQuery}, not {@code null}
     * @return {@code true}, if such an {@link ExchangeRate} is currently
     * defined.
     */
    boolean isAvailable(ConversionQuery conversionQuery);


    /**
     * Access a {@link ExchangeRate} using the given currencies. The
     * {@link ExchangeRate} may be, depending on the data provider, eal-time or
     * deferred. This method should return the rate that is <i>currently</i>
     * valid.
     *
     * @param base base {@link javax.money.CurrencyUnit}, not {@code null}
     * @param term term {@link javax.money.CurrencyUnit}, not {@code null}
     * @throws CurrencyConversionException If no such rate is available.
     */
    ExchangeRate getExchangeRate(CurrencyUnit base, CurrencyUnit term);

    /**
     * Access a {@link CurrencyConversion} that can be applied as a
     * {@link javax.money.MonetaryOperator} to an amount.
     *
     * @param term term {@link CurrencyUnit}, not {@code null}
     * @return a new instance of a corresponding {@link CurrencyConversion},
     * never {@code null}.
     */
    CurrencyConversion getCurrencyConversion(CurrencyUnit term);

    /**
     * Checks if an {@link ExchangeRate} between two {@link CurrencyUnit} is
     * available from this provider. This method should check, if a given rate
     * is <i>currently</i> defined.
     *
     * @param base the base {@link CurrencyUnit}
     * @param term the term {@link CurrencyUnit}
     * @return {@code true}, if such an {@link ExchangeRate} is currently
     * defined.
     */
    boolean isAvailable(CurrencyUnit base, CurrencyUnit term);


    /**
     * Checks if an {@link ExchangeRate} between two {@link CurrencyUnit} is
     * available from this provider. This method should check, if a given rate
     * is <i>currently</i> defined.
     *
     * @param baseCode the base currency code
     * @param termCode the terminal/target currency code
     * @return {@code true}, if such an {@link ExchangeRate} is currently
     * defined.
     * @throws javax.money.MonetaryException if one of the currency codes passed is not valid.
     */
    boolean isAvailable(String baseCode, String termCode);


    /**
     * Access a {@link ExchangeRate} using the given currencies. The
     * {@link ExchangeRate} may be, depending on the data provider, eal-time or
     * deferred. This method should return the rate that is <i>currently</i>
     * valid.
     *
     * @param baseCode base currency code, not {@code null}
     * @param termCode term/target currency code, not {@code null}
     * @return the matching {@link ExchangeRate}.
     * @throws CurrencyConversionException If no such rate is available.
     * @throws javax.money.MonetaryException           if one of the currency codes passed is not valid.
     */
    ExchangeRate getExchangeRate(String baseCode, String termCode);


    /**
     * The method reverses the {@link ExchangeRate} to a rate mapping from term
     * to base {@link CurrencyUnit}. Hereby the factor must <b>not</b> be
     * recalculated as {@code 1/oldFactor}, since typically reverse rates are
     * not symmetric in most cases.
     *
     * @return the matching reversed {@link ExchangeRate}, or {@code null}, if
     * the rate cannot be reversed.
     */
    ExchangeRate getReversed(ExchangeRate rate);

    /**
     * Access a {@link CurrencyConversion} that can be applied as a
     * {@link javax.money.MonetaryOperator} to an amount.
     *
     * @param termCode terminal/target currency code, not {@code null}
     * @return a new instance of a corresponding {@link CurrencyConversion},
     * never {@code null}.
     * @throws javax.money.MonetaryException if one of the currency codes passed is not valid.
     */
    CurrencyConversion getCurrencyConversion(String termCode);

}
