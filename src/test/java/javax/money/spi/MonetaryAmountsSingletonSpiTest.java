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

import org.testng.annotations.Test;

import javax.money.DummyAmount;
import javax.money.DummyAmountBuilder;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Tests the default methods on MonetaryAmountsSingletonSpi.
 */
public class MonetaryAmountsSingletonSpiTest {

    private MonetaryAmountsSingletonSpi testSpi = new BaseMonetaryAmountsSingletonSpi() {
        @SuppressWarnings("unchecked")
        @Override
        public <T extends MonetaryAmount> MonetaryAmountFactory<T> getAmountFactory(Class<T> amountType) {
            if (amountType.equals(DummyAmount.class)) {
                return (MonetaryAmountFactory<T>) new DummyAmountBuilder();
            }
            return null;
        }

        @Override
        public Class<? extends MonetaryAmount> getDefaultAmountType() {
            return DummyAmount.class;
        }

        @Override
        public Collection<Class<? extends MonetaryAmount>> getAmountTypes() {
            Set<Class<? extends MonetaryAmount>> result = new HashSet<>();
            result.add(DummyAmount.class);
            return result;
        }
    };

    @Test
    public void testGetDefaultAmountFactory() {
        assertNotNull(testSpi.getDefaultAmountFactory());
        assertEquals(DummyAmountBuilder.class, testSpi.getDefaultAmountFactory().getClass());
    }

    @Test
    public void testGetAmountFactories() {
        Collection<MonetaryAmountFactory<?>> factories = testSpi.getAmountFactories();
        assertNotNull(factories);
        assertFalse(factories.isEmpty());
        assertEquals(1, factories.size());
        assertEquals(DummyAmountBuilder.class, factories.iterator().next().getClass());
    }

}
