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

import javax.money.CurrencyQuery;
import javax.money.CurrencyQueryBuilder;
import javax.money.CurrencyUnit;
import javax.money.TestCurrency;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Test class to test the default methods of CurrencyProviderSpi.
 */
public class CurrencyProviderSpiTest {

    private static CurrencyProviderSpi testProvider = new CurrencyProviderSpi(){
        /**
         * The unique name of this currency provider instance.
         * @return hte unique provider id, never null or empty.
         */
        public String getProviderName(){
            return getClass().getSimpleName();
        }

        /**
         * CHecks if a {@link javax.money.CurrencyUnit} instances matching the given
         * {@link javax.money.CurrencyContext} is available from this provider.
         *
         * @param query the {@link javax.money.CurrencyQuery} containing the parameters determining the query. not null.
         * @return false, if no such unit is provided by this provider.
         */
        public boolean isCurrencyAvailable(CurrencyQuery query){
            return !getCurrencies(query).isEmpty();
        }

        @Override
        public Set<CurrencyUnit> getCurrencies(CurrencyQuery query) {
            Set<CurrencyUnit> result = new HashSet<>();
            if (query.getCurrencyCodes().contains("CHF")) {
                result.add(TestCurrency.of("CHF"));
            }
            return result;
        }

    };

    @Test
    public void testGetProviderName() throws Exception {
        assertEquals(testProvider.getProviderName(), testProvider.getClass().getSimpleName());

    }

    @Test
    public void testIsCurrencyAvailable() throws Exception {
        assertTrue(testProvider.isCurrencyAvailable(CurrencyQueryBuilder.of().setCurrencyCodes("CHF").build()));
        assertFalse(testProvider.isCurrencyAvailable(CurrencyQueryBuilder.of().setCurrencyCodes("foofoo").build()));
    }

}