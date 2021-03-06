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
import javax.money.MonetaryAmountFactoryQuery;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * SPI (core) for the backing implementation of the {@link javax.money.Monetary} singleton, implementing
 * the query functionality for amounts.
 *
 * @author Anatole Tresch
 */
public abstract class BaseMonetaryAmountsSingletonQuerySpi implements MonetaryAmountsSingletonQuerySpi{

    /**
     * Checks if an {@link javax.money.MonetaryAmountFactory} is matching the given query.
     *
     * @param query the factory query, not null.
     * @return true, if at least one {@link javax.money.MonetaryAmountFactory} matches the query.
     */
    public boolean isAvailable(MonetaryAmountFactoryQuery query) {
        return !getAmountFactories(query).isEmpty();
    }

    /**
     * Executes the query and returns the {@link javax.money.MonetaryAmount} implementation type found,
     * if there is only one type.
     * If multiple types match the query, the first one is selected.
     *
     * @param query the factory query, not null.
     * @return the type found, or null.
     */
    public Class<? extends MonetaryAmount> getAmountType(MonetaryAmountFactoryQuery query) {
        MonetaryAmountFactory<?> f = getAmountFactory(query);
        if (f != null) {
            return f.getAmountType();
        }
        return null;
    }

    /**
     * Executes the query and returns the {@link javax.money.MonetaryAmount} implementation types found.
     *
     * @param query the factory query, not null.
     * @return the type found, or null.
     */
    @SuppressWarnings("unchecked")
    public Collection<Class<? extends MonetaryAmount>> getAmountTypes(MonetaryAmountFactoryQuery query) {
        Collection<MonetaryAmountFactory<? extends MonetaryAmount>> factories = getAmountFactories(query);
        Set<Class<? extends MonetaryAmount>> result = new HashSet<>();
        for(MonetaryAmountFactory f:factories){
            result.add(f.getAmountType());
        }
        return result;
    }

    /**
     * Executes the query and returns the {@link javax.money.MonetaryAmountFactory} implementation type found,
     * if there is only one type. If multiple types match the query, the first one is selected.
     *
     * @param query the factory query, not null.
     * @return the type found, or null.
     */
    public MonetaryAmountFactory getAmountFactory(MonetaryAmountFactoryQuery query) {
        Collection<MonetaryAmountFactory<?>> factories = getAmountFactories(query);
        if (factories.isEmpty()) {
            return null;
        }
        return factories.iterator().next();
    }

}
