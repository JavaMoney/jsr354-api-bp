/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.spi;

import org.javamoney.bp.MonetaryException;
import org.javamoney.bp.format.AmountFormatQuery;
import org.javamoney.bp.format.MonetaryAmountFormat;
import java.util.*;

/**
 * This interface models the singleton functionality of {@link org.javamoney.bp.format.MonetaryFormats}.
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
     * Access all {@link org.javamoney.bp.format.MonetaryAmountFormat} instances matching the given {@link org.javamoney.bp.format
     * .AmountFormatQuery}.
     *
     * @param formatQuery The format query defining the requirements of the formatters.
     * @return the corresponding {@link org.javamoney.bp.format.MonetaryAmountFormat} instances, never null
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
     * Access an {@link org.javamoney.bp.format.MonetaryAmountFormat} given a {@link org.javamoney.bp.format
     * .AmountFormatQuery}.
     *
     * @param formatQuery The format query defining the requirements of the formatter.
     * @return the corresponding {@link org.javamoney.bp.format.MonetaryAmountFormat}
     * @throws org.javamoney.bp.MonetaryException if no registered {@link org.javamoney.bp.spi
     *                                       .MonetaryAmountFormatProviderSpi} can provide a
     *                                       corresponding {@link org.javamoney.bp.format.MonetaryAmountFormat} instance.
     */
    MonetaryAmountFormat getAmountFormat(AmountFormatQuery formatQuery);

    /**
     * Checks if a {@link org.javamoney.bp.format.MonetaryAmountFormat} is available given a {@link org.javamoney.bp.format
     * .AmountFormatQuery}.
     *
     * @param formatQuery The format query defining the requirements of the formatter.
     * @return true, if a t least one {@link org.javamoney.bp.format.MonetaryAmountFormat} is matching the query.
     */
    boolean isAvailable(AmountFormatQuery formatQuery);

    /**
     * Checks if a {@link org.javamoney.bp.format.MonetaryAmountFormat} is available given a {@link org.javamoney.bp.format
     * .AmountFormatQuery}.
     *
     * @param locale    the target {@link java.util.Locale}, not {@code null}.
     * @param providers The (optional) providers to be used, oredered correspondingly.
     * @return true, if a t least one {@link org.javamoney.bp.format.MonetaryAmountFormat} is matching the query.
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
