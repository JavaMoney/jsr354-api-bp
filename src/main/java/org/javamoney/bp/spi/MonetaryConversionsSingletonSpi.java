/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2015, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.spi;

import java.util.*;

import org.javamoney.bp.CurrencyUnit;
import org.javamoney.bp.MonetaryException;
import org.javamoney.bp.convert.ConversionQuery;
import org.javamoney.bp.convert.CurrencyConversion;
import org.javamoney.bp.convert.ExchangeRateProvider;

/**
 * SPI (conversoin) that implements the functionalities provided by the
 * {@code MonetaryConversions} singleton accessor. It should be registered as a
 * service using the JDK {@code ServiceLoader}. Hereby only one instance can be
 * registered at a time.
 * <p>
 * This interface is designed to support also contextual behaviour, e.g. in Java
 * EE containers each application may provide its own
 * {@link org.javamoney.bp.convert.ExchangeRateProvider} instances, e.g. by registering them as CDI
 * beans. An EE container can register an according
 * {@link MonetaryConversionsSingletonSpi} that manages the different application
 * contexts transparently. In a SE environment this class is expected to behave
 * like an ordinary singleton, loading its SPIs from the {@link java.util.ServiceLoader}.
 * <p>
 * Instances of this class must be thread safe. It is not a requirement that
 * they are serializable.
 * <p>
 * Only one instance can be registered using the {@link java.util.ServiceLoader}. When
 * registering multiple instances the {@link org.javamoney.bp.convert.MonetaryConversions} accessor will
 * not work.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public interface MonetaryConversionsSingletonSpi {

    /**
     * Get all currently registered provider names.
     *
     * @return all currently registered provider names
     * @see org.javamoney.bp.convert.ProviderContext#getProviderName()
     */
    Collection<String> getProviderNames();


    /**
     * Get the default provider chain used. The ordering of the items is the
     * access order/precedence of the providers.
     *
     * @return the default provider chain, not {@code null} and not empty.
     */
    List<String> getDefaultProviderChain();

    /**
     * Access an instance of {@link org.javamoney.bp.convert.ExchangeRateProvider}. By setting {@link org.javamoney.bp.convert
     * .ConversionQuery#getProviderNames()} multiple providers can be selected,
     * that will be included into a <i>compound</i> instance, with the same order as returned by the {@link javax
     * .money.convert.ConversionQuery}.
     *
     * @param conversionQuery the {@link org.javamoney.bp.convert.ConversionQuery} determining the tpye of conversion
     *                        required, not null.
     * @return an {@link org.javamoney.bp.convert.ExchangeRateProvider} built up with the given sub
     * providers, never {@code null}
     * @throws MonetaryException if a provider could not be found.
     * @see #isExchangeRateProviderAvailable(org.javamoney.bp.convert.ConversionQuery)
     */
    ExchangeRateProvider getExchangeRateProvider(ConversionQuery conversionQuery);

    /**
     * Allows to quickly check, if a {@link org.javamoney.bp.convert.ExchangeRateProvider} is accessible for the given
     * {@link org.javamoney.bp.convert.ConversionQuery}.
     *
     * @param conversionQuery the {@link org.javamoney.bp.convert.ConversionQuery} determining the tpye of conversion
     *                        required, not null.
     * @return {@code true}, if such a conversion is supported, meaning an according
     * {@link ExchangeRateProvider} can be
     * accessed.
     * @see #getExchangeRateProvider(ConversionQuery)
     * @see #getExchangeRateProvider(String...)}
     */
    boolean isExchangeRateProviderAvailable(ConversionQuery conversionQuery);

    /**
     * Allows to quickly check, if a {@link org.javamoney.bp.convert.CurrencyConversion} is accessible for the given
     * {@link org.javamoney.bp.convert.ConversionQuery}.
     *
     * @param conversionQuery the {@link org.javamoney.bp.convert.ConversionQuery} determining the tpye of conversion
     *                        required, not null.
     * @return {@code true}, if such a conversion is supported, meaning an according
     * {@link CurrencyConversion} can be
     * accessed.
     * @see #getConversion(org.javamoney.bp.convert.ConversionQuery)
     * @see #getConversion(CurrencyUnit, String...)}
     */
    boolean isConversionAvailable(ConversionQuery conversionQuery);
    /**
     * Allows to quickly check, if a {@link org.javamoney.bp.convert.CurrencyConversion} is accessible for the given
     * {@link org.javamoney.bp.convert.ConversionQuery}.
     *
     * @param termCurrency the terminating/target currency unit, not null.
     * @param providers    the provider names defines a corresponding
     *                     prpovider chain that must be encapsulated by the resulting {@link javax
     *                     .money.convert.CurrencyConversion}. By default the provider
     *                     chain as defined by #getDefaultProviderChain will be used.
     * @return {@code true}, if such a conversion is supported, meaning an according
     * {@link CurrencyConversion} can be
     * accessed.
     * @see #getConversion(org.javamoney.bp.convert.ConversionQuery)
     * @see #getConversion(CurrencyUnit, String...)}
     */
    boolean isConversionAvailable(CurrencyUnit termCurrency, String... providers);
    /**
     * Access the current registered {@link org.javamoney.bp.convert.ExchangeRateProvider} instances. If no provider
     * names are passed ALL current registered providers are returned in undefined order.
     *
     * @param providers the provider names of hte providers to be accessed
     * @return the list of providers, in the same order as requested.
     * @throws MonetaryException if a provider could not be resolved.
     */
    List<ExchangeRateProvider> getExchangeRateProviders(String... providers);

    /**
     * Access a compound instance of an {@link ExchangeRateProvider} based on the given provider chain.
     *
     * @param providers the {@link org.javamoney.bp.convert.ConversionQuery} provider names defines a corresponding
     *                  prpovider chain that must be
     *                  encapsulated by the resulting {@link org.javamoney.bp.convert.ExchangeRateProvider}. By default
     *                  the default
     *                  provider changes as defined in #getDefaultProviderChain will be used.
     * @return an {@link ExchangeRateProvider} built up with the given sub
     * providers, never {@code null}.
     * @throws MonetaryException if a provider listed could not be found.
     * @see #getProviderNames()
     * @see #isExchangeRateProviderAvailable(org.javamoney.bp.convert.ConversionQuery)
     */
    ExchangeRateProvider getExchangeRateProvider(String... providers);

    /**
     * Access an instance of {@link CurrencyConversion}.
     *
     * @param conversionQuery the {@link org.javamoney.bp.convert.ConversionQuery} determining the tpye of conversion
     *                        required, not null.
     * @return the corresponding conversion, not null.
     * @throws org.javamoney.bp.MonetaryException if no matching conversion could be found.
     * @see #isConversionAvailable(org.javamoney.bp.convert.ConversionQuery)
     */
    CurrencyConversion getConversion(ConversionQuery conversionQuery);

    /**
     * Access an instance of {@link CurrencyConversion}.
     *
     * @param termCurrency the terminating/target currency unit, not null.
     * @param providers    the {@link org.javamoney.bp.convert.ConversionQuery} provider names defines a corresponding
     *                     prpovider chain that must be encapsulated by the resulting {@link javax
     *                     .money.convert.CurrencyConversion}. By default the default
     *                     provider chain as defined by #getDefaultProviderChain will be used.
     * @return the correspöonding conversion, not null.
     * @throws org.javamoney.bp.MonetaryException if no matching conversion could be found.
     * @see #isConversionAvailable(org.javamoney.bp.convert.ConversionQuery)
     */
    CurrencyConversion getConversion(CurrencyUnit termCurrency, String... providers);
}
