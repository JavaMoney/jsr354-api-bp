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
import javax.money.MonetaryContext;

/**
 * SPI (core): Implementations of this interface are used by the {@link MonetaryAmountsSingletonSpi} to evaluate the
 * correct {@link javax.money.MonetaryAmountFactory} instances.
 *
 * @param <T> the concrete amount type.
 * @author Anatole Tresch
 */
public abstract class BaseMonetaryAmountFactoryProviderSpi<T extends MonetaryAmount>
implements MonetaryAmountFactoryProviderSpi<T>{

    /**
     * Method that determines if this factory should be considered for general evaluation of
     * matching {@link javax.money.MonetaryAmount} implementation types when calling
     * {@link javax.money.Monetary#getAmountFactory(javax.money.MonetaryAmountFactoryQuery)}.
     *
     * @return {@code true} to include this factory into the evaluation.
     * @see javax.money.Monetary#getAmountFactory(javax.money.MonetaryAmountFactoryQuery)
     */
    public QueryInclusionPolicy getQueryInclusionPolicy(){
        return QueryInclusionPolicy.ALWAYS;
    }

    /**
     * Returns the maximal {@link javax.money.MonetaryContext} supported, for requests that exceed these maximal
     * capabilities, an {@link ArithmeticException} must be thrown.
     *
     * @return the maximal {@link javax.money.MonetaryContext} supported, never {@code null}
     */
    public MonetaryContext getMaximalMonetaryContext(){
        return getDefaultMonetaryContext();
    }

}
