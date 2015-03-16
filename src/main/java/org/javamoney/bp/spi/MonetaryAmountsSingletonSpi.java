/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.spi;

import org.javamoney.bp.MonetaryAmount;
import org.javamoney.bp.MonetaryAmountFactory;
import org.javamoney.bp.MonetaryAmounts;
import org.javamoney.bp.MonetaryException;
import java.util.Collection;

/**
 * SPI (core) for the backing implementation of the {@link org.javamoney.bp.MonetaryAmounts} singleton. It
 * should load and manage (including contextual behavior), if needed) the different registered
 * {@link org.javamoney.bp.MonetaryAmountFactory} instances.
 *
 * @author Anatole Tresch
 */
public interface MonetaryAmountsSingletonSpi{

    /**
     * Access the {@link org.javamoney.bp.MonetaryAmountFactory} for the given {@code amountType} .
     *
     * @param amountType the {@link MonetaryAmount} implementation type, targeted by the factory.
     * @return the {@link org.javamoney.bp.MonetaryAmountFactory}, or {@code null}, if no such
     * {@link org.javamoney.bp.MonetaryAmountFactory} is available in the current context.
     */
    <T extends MonetaryAmount> MonetaryAmountFactory<T> getAmountFactory(Class<T> amountType);

    /**
     * Access the default {@link MonetaryAmount} implementation type.
     *
     * @return a the default {@link MonetaryAmount} type corresponding, never {@code null}.
     * @throws MonetaryException if no {@link MonetaryAmountFactoryProviderSpi} is available, or no
     *                           {@link MonetaryAmountFactoryProviderSpi} targeting the configured default
     *                           {@link MonetaryAmount} type.
     * @see MonetaryAmounts#getDefaultAmountType()
     */
    Class<? extends MonetaryAmount> getDefaultAmountType();

    /**
     * Get the currently registered {@link MonetaryAmount} implementation types.
     *
     * @return the {@link java.util.Set} if registered {@link MonetaryAmount} implementations, never
     * {@code null}.
     */
    Collection<Class<? extends MonetaryAmount>> getAmountTypes();


    /**
     * Access the default {@link org.javamoney.bp.MonetaryAmountFactory}.
     *
     * @return a the default {@link MonetaryAmount} type corresponding, never {@code null}.
     * @throws MonetaryException if no {@link MonetaryAmountFactoryProviderSpi} is available, or no
     *                           {@link MonetaryAmountFactoryProviderSpi} targeting the configured default
     *                           {@link MonetaryAmount} type.
     * @see MonetaryAmounts#getDefaultAmountType()
     */
    MonetaryAmountFactory<?> getDefaultAmountFactory();

    /**
     * Get the currently registered {@link MonetaryAmount} implementation classes.
     *
     * @return the {@link java.util.Set} if registered {@link MonetaryAmount} implementations, never
     * {@code null}.
     */
    Collection<MonetaryAmountFactory<?>> getAmountFactories();

}