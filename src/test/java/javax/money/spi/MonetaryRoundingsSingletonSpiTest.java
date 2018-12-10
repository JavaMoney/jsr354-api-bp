/*
 * Copyright 2012-2016 Credit Suisse
 * Copyright 2018 Werner Keil, Otavio Santana, Trivadis AG
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

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryException;
import javax.money.MonetaryRounding;
import javax.money.RoundingContext;
import javax.money.RoundingQuery;
import javax.money.RoundingQueryBuilder;
import javax.money.TestCurrency;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.AssertJUnit.*;

/**
 * Tests the default methods on MonetaryAmountsSingletonSpi.
 */
public class MonetaryRoundingsSingletonSpiTest {

    private static final MonetaryRounding R1 = new MonetaryRounding() {
        @Override
        public RoundingContext getRoundingContext() {
            return null;
        }

        @Override
        public MonetaryAmount apply(MonetaryAmount monetaryAmount) {
            return monetaryAmount;
        }
    };

    private MonetaryRoundingsSingletonSpi testSpi = new BaseMonetaryRoundingsSingletonSpi() {

        @Override
        public Set<String> getRoundingNames(String... providers) {
            Set<String> roundings = new HashSet<>();
            roundings.add("r1");
            return roundings;
        }

        @Override
        public Set<String> getProviderNames() {
            Set<String> providers = new HashSet<>();
            providers.add("p");
            return providers;
        }

        @Override
        public List<String> getDefaultProviderChain() {
            List<String> providers = new ArrayList<>();
            providers.add("a");
            return providers;
        }

        @Override
        public Collection<MonetaryRounding> getRoundings(RoundingQuery query) {
            List<MonetaryRounding> roundings = new ArrayList<>();
            if ("r1".equals(query.getRoundingName())) {
                roundings.add(R1);
            } else if (query.getCurrency() != null && query.getCurrency().getCurrencyCode().equals("USD")) {
                roundings.add(R1);
            }
            return roundings;
        }

        @Override
        public MonetaryRounding getDefaultRounding() {
            return R1;
        }

        /**
         * Access a {@link javax.money.MonetaryRounding} for rounding {@link javax.money.MonetaryAmount}
         * instances given a currency.
         *
         * @param currencyUnit The currency, which determines the required precision. As
         *                     {@link java.math.RoundingMode}, by default, {@link java.math.RoundingMode#HALF_UP}
         *                     is sued.
         * @param providers    the optional provider list and ordering to be used
         * @return a new instance {@link javax.money.MonetaryOperator} implementing the
         * rounding, never {@code null}.
         * @throws javax.money.MonetaryException if no such rounding could be provided.
         */
        public MonetaryRounding getRounding(CurrencyUnit currencyUnit, String... providers) {
            MonetaryRounding op =
                    getRounding(RoundingQueryBuilder.of().setProviderNames(providers).setCurrency(currencyUnit).build());
            if(op==null) {
                throw new MonetaryException(
                        "No rounding provided for CurrencyUnit: " + currencyUnit.getCurrencyCode());
            }
            return op;
        }

    };


    @Test
    public void testGetRounding_CurrencyUnit_StringArr() {
        MonetaryRounding r = testSpi.getRounding(TestCurrency.of("USD"), "p");
        assertNotNull(r);
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetRounding_CurrencyUnit_StringArr_BC() {
        testSpi.getRounding(TestCurrency.of("EUR"), "p");
    }

    @Test
    public void testGetRounding_String_StringArr() {
        assertNotNull(testSpi.getRounding("r1"));
        assertNotNull(testSpi.getRounding("r1", "p"));
    }

    @Test
    public void testGetRounding_RoundingQuery() {
        assertNotNull(testSpi.getRounding(RoundingQueryBuilder.of().setRoundingName("r1").build()));
    }

    @Test
    public void tesIsRoundingAvailable() {
        assertTrue(testSpi.isRoundingAvailable(RoundingQueryBuilder.of().setRoundingName("r1").build()));
        assertFalse(testSpi.isRoundingAvailable(RoundingQueryBuilder.of().setRoundingName("hgfhgf").build()));
    }

    @Test
    public void tesIsRoundingAvailable_String_StringArr() {
        assertTrue(testSpi.isRoundingAvailable("r1"));
        assertFalse(testSpi.isRoundingAvailable("sdsd"));
    }

    @Test
    public void tesIsRoundingAvailable_CurrencyUnit_StringArr() {
        assertTrue(testSpi.isRoundingAvailable(TestCurrency.of("USD"), "p"));
        assertTrue(testSpi.isRoundingAvailable(TestCurrency.of("USD")));
        assertFalse(testSpi.isRoundingAvailable(TestCurrency.of("CHF")));
        assertFalse(testSpi.isRoundingAvailable(TestCurrency.of("CHF"), "p"));
    }

}
