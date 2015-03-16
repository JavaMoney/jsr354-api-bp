/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.api.spi;

import org.javamoney.bp.api.MonetaryAmount;
import org.javamoney.bp.api.MonetaryAmountFactory;
import org.javamoney.bp.api.MonetaryAmounts;
import org.javamoney.bp.api.MonetaryContext;

/**
 * SPI (core): Implementations of this interface are used by the {@link MonetaryAmountsSingletonSpi} to evaluate the
 * correct {@link org.javamoney.bp.MonetaryAmountFactory} instances.
 *
 * @param <T> the concrete amount type.
 * @author Anatole Tresch
 */
public interface MonetaryAmountFactoryProviderSpi<T extends MonetaryAmount>{

    /**
     * Determines how the factory should be considered when querying for matching implementation
     * types calling {@link MonetaryAmounts#getAmountFactory(org.javamoney.bp.MonetaryAmountFactoryQuery)} .
     *
     * @see MonetaryAmounts#getAmountFactory(org.javamoney.bp.MonetaryAmountFactoryQuery)
     */
    enum QueryInclusionPolicy{
        /**
         * Always include this factory (and the corresponding amount type) within queries. This is
         * the default for normal {@link MonetaryAmount} implementation types.
         */
        ALWAYS,
        /**
         * Only consider this factory, when the implementation type is specified explicitly in the
         * {@link MonetaryContext} required.
         */
        DIRECT_REFERENCE_ONLY,
        /**
         * Never consider this factory in a query for a matching {@link MonetaryAmount}
         * implementation.
         */
        NEVER
    }


    /**
     * Method that determines if this factory should be considered for general evaluation of
     * matching {@link MonetaryAmount} implementation types when calling
     * {@link MonetaryAmounts#getAmountFactory(org.javamoney.bp.MonetaryAmountFactoryQuery)}.
     *
     * @return {@code true} to include this factory into the evaluation.
     * @see MonetaryAmounts#getAmountFactory(org.javamoney.bp.MonetaryAmountFactoryQuery)
     */
    QueryInclusionPolicy getQueryInclusionPolicy();

    /**
     * Get the concrete amount type created by {@link org.javamoney.bp.MonetaryAmountFactory} instances provided.
     *
     * @return the concrete amount type created, never null.
     */
    Class<T> getAmountType();

    /**
     * Access a {@link org.javamoney.bp.MonetaryAmountFactory} given the required context.
     *
     * @return the corresponding {@link org.javamoney.bp.MonetaryAmountFactory}, or {@code null}.
     */
    MonetaryAmountFactory<T> createMonetaryAmountFactory();

    /**
     * Returns the default {@link MonetaryContext} used, when no {@link MonetaryContext} is
     * provided.
     * <p>
     * The default context is not allowed to exceed the capabilities of the maximal
     * {@link MonetaryContext} supported.
     *
     * @return the default {@link MonetaryContext}, never {@code null}.
     * @see #getMaximalMonetaryContext()
     */
    MonetaryContext getDefaultMonetaryContext();

    /**
     * Returns the maximal {@link MonetaryContext} supported, for requests that exceed these maximal
     * capabilities, an {@link ArithmeticException} must be thrown.
     *
     * @return the maximal {@link MonetaryContext} supported, never {@code null}
     */
    MonetaryContext getMaximalMonetaryContext();

}
