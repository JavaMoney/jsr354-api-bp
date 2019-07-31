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

import static javax.money.spi.MonetaryAmountFactoryProviderSpi.QueryInclusionPolicy;
import static javax.money.spi.MonetaryAmountFactoryProviderSpi.QueryInclusionPolicy.ALWAYS;

import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;

/**
 * unit test.
 */
public class MonetaryAmountFactoryProviderSpiTest {

    private MonetaryAmountFactoryProviderSpiTestStub sut;
    private MonetaryContext monetaryContext;

    @BeforeMethod
    public void setUp() throws Exception {
        monetaryContext = MonetaryContextBuilder.of().build();
        sut = new MonetaryAmountFactoryProviderSpiTestStub(monetaryContext);
    }

    @Test
    public void shouldReturnQueryInclusionPolicyALWAYS() throws Exception {
        // when
        QueryInclusionPolicy result = sut.getQueryInclusionPolicy();
        // then
        AssertJUnit.assertEquals(result, ALWAYS);
    }

    @Test
    public void getMaximalMonetaryContextShouldReturnDefault() throws Exception {
        // given
        // when
        MonetaryContext result = sut.getMaximalMonetaryContext();
        // then
        AssertJUnit.assertTrue(result == monetaryContext);
    }

    private static final class MonetaryAmountFactoryProviderSpiTestStub extends BaseMonetaryAmountFactoryProviderSpi {

        private final MonetaryContext monetaryContext;

        public MonetaryAmountFactoryProviderSpiTestStub(MonetaryContext monetaryContext) {
            this.monetaryContext = monetaryContext;
        }

        @Override
        public Class getAmountType() {
            return null;
        }

        @Override
        public MonetaryAmountFactory createMonetaryAmountFactory() {
            return null;
        }

        @Override
        public MonetaryContext getDefaultMonetaryContext() {
            return monetaryContext;
        }
    }
}
