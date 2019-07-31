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

import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.Monetary;

/**
 * Factory for {@link javax.money.MonetaryAmount} instances for a given type. It can be accessed, by
 * <ul>
 * <li>calling {@link javax.money.MonetaryAmount#getFactory()}, returning a {@link BaseMonetaryAmountFactory}
 * creating amounts of the same implementation type, which also provided the factory instance.</li>
 * <li>calling {@link javax.money.Monetary#getAmountFactory(Class)} accessing a
 * {@link BaseMonetaryAmountFactory} for a concrete type <code>Class<T></code>.</li>
 * <li>calling {@link javax.money.Monetary#getDefaultAmountFactory()} accessing a default
 * {@link BaseMonetaryAmountFactory}.
 * </ul>
 * <p>
 * Implementations of this interface allow to get {@link javax.money.MonetaryAmount} instances providing
 * different data as required:
 * <ul>
 * <li>the {@link javax.money.CurrencyUnit}, or the corresponding currency code (must be solvable by
 * {@link javax.money.Monetary}).</li>
 * <li>the number part</li>
 * <li>the {@link javax.money.MonetaryContext}</li>
 * <li>by passing any {@link javax.money.MonetaryAmount} instance, it is possible to convert an arbitrary amount
 * implementation to the implementation provided by this factory. If the current factory cannot
 * support the precision/scale as required by the current {@link javax.money.NumberValue} a
 * {@link javax.money.MonetaryException} must be thrown.</li>
 * </ul>
 * If not defined a default {@link javax.money.MonetaryContext} is used, which can also be configured by adding
 * configuration to a file {@code /javamoney.properties} to the classpath.
 * <p>
 * Hereby the entries. e.g. for a class {@code MyMoney} should start with {@code a.b.MyMoney.ctx}. The entries valid
 * must be documented
 * on the according implementation class, where the following entries are defined for all implementation types
 * (example below given for a class {@code a.b.MyMoney}:
 * <ul>
 * <li>{@code a.b.MyMoney.ctx.precision} to define the maximal supported precision.</li>
 * <li>{@code a.b.MyMoney.ctx.maxScale} to define the maximal supported scale.</li>
 * <li>{@code a.b.MyMoney.ctx.fixedScale} to define the scale to be fixed (constant).</li>
 * </ul>
 * <p>
 * <h2>Implementation specification</h2> Instances of this interface are <b>not</b> required to be
 * thread-safe!
 *
 * @author Anatole Tresch
 * @author Werner Keil
 * @version 0.6.1
 */
public abstract class BaseMonetaryAmountFactory<T extends MonetaryAmount> implements MonetaryAmountFactory<T> {

    /**
     * Sets the {@link javax.money.CurrencyUnit} to be used.
     *
     * @param currencyCode the currencyCode of the currency to be used, not {@code null}. The currency code
     *                     will be resolved using {@link javax.money.Monetary#getCurrency(String, String...)}.
     * @return This factory instance, for chaining.
     * @throws javax.money.UnknownCurrencyException if the {@code currencyCode} is not resolvable.
     */
    public MonetaryAmountFactory<T> setCurrency(String currencyCode) {
        return setCurrency(Monetary.getCurrency(currencyCode));
    }

    /**
     * Uses an arbitrary {@link javax.money.MonetaryAmount} to initialize this factory. Properties reused are:
     * <ul>
     * <li>CurrencyUnit,</li>
     * <li>Number value,</li>
     * <li>MonetaryContext.</li>
     * </ul>
     *
     * @param amount the amount to be used, not {@code null}.
     * @return this factory instance, for chaining.
     * @throws javax.money.MonetaryException when the {@link javax.money.MonetaryContext} implied by {@code amount.getContext()}
     *                           exceeds the capabilities supported by this factory type.
     */
    public MonetaryAmountFactory<T> setAmount(MonetaryAmount amount) {
        setCurrency(amount.getCurrency());
        setNumber(amount.getNumber());
        setContext(amount.getContext());
        return this;
    }

    /**
     * Returns the maximal {@link javax.money.MonetaryContext} supported, for requests that exceed these maximal
     * capabilities, an {@link ArithmeticException} must be thrown.
     *
     * @return the maximal {@link javax.money.MonetaryContext} supported, never {@code null}
     */
    public MonetaryContext getMaximalMonetaryContext() {
        return getDefaultMonetaryContext();
    }

}