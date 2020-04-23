/*
 * Copyright 2012-2016 Credit Suisse
 * Copyright 2018-2020 Werner Keil, Otavio Santana, Trivadis AG
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SPI (core) for the backing implementation of the {@link javax.money.Monetary} singleton. It
 * should load and manage (including contextual behavior), if needed) the different registered
 * {@link javax.money.MonetaryAmountFactory} instances.
 *
 * @author Anatole Tresch
 */
public abstract class BaseMonetaryAmountsSingletonSpi implements MonetaryAmountsSingletonSpi{

    /**
     * Access the default {@link javax.money.MonetaryAmountFactory}.
     *
     * @return a the default {@link javax.money.MonetaryAmount} type corresponding, never {@code null}.
     * @throws javax.money.MonetaryException if no {@link MonetaryAmountFactoryProviderSpi} is available, or no
     *                           {@link MonetaryAmountFactoryProviderSpi} targeting the configured default
     *                           {@link javax.money.MonetaryAmount} type.
     * @see javax.money.Monetary#getDefaultAmountType()
     */
    public MonetaryAmountFactory<?> getDefaultAmountFactory(){
        return getAmountFactory(getDefaultAmountType());
    }

    /**
     * Get the currently registered {@link javax.money.MonetaryAmount} implementation classes.
     *
     * @return the {@link java.util.Set} if registered {@link javax.money.MonetaryAmount} implementations, never
     * {@code null}.
     */
    public Collection<MonetaryAmountFactory<?>> getAmountFactories(){
        List<MonetaryAmountFactory<?>> factories = new ArrayList<>();
        for(Class<? extends MonetaryAmount> type : getAmountTypes()){
            factories.add(getAmountFactory(type));
        }
        return factories;
    }

}