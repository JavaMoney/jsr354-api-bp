/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.spi;

import static org.javamoney.bp.spi.MonetaryAmountFactoryProviderSpi.QueryInclusionPolicy;
import static org.javamoney.bp.spi.MonetaryAmountFactoryProviderSpi.QueryInclusionPolicy.ALWAYS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.javamoney.bp.MonetaryAmountFactory;
import org.javamoney.bp.MonetaryContext;
import org.javamoney.bp.MonetaryContextBuilder;

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
        assertThat(result, is(ALWAYS));
    }

    @Test
    public void getMaximalMonetaryContextShouldReturnDefault() throws Exception {
        // given
        // when
        MonetaryContext result = sut.getMaximalMonetaryContext();
        // then
        assertThat(result, sameInstance(monetaryContext));
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
