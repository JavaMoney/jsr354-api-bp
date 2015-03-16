/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE.
 *
 * Specification: JSR-354 Money and Currency API ("Specification")
 *
 * Copyright (c) 2012-2014, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.api.spi;

import org.javamoney.bp.api.CurrencyQuery;
import org.javamoney.bp.api.CurrencyQueryBuilder;
import org.javamoney.bp.api.CurrencyUnit;
import org.javamoney.bp.api.TestCurrency;
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
         * CHecks if a {@link org.javamoney.bp.CurrencyUnit} instances matching the given
         * {@link org.javamoney.bp.CurrencyContext} is available from this provider.
         *
         * @param query the {@link org.javamoney.bp.CurrencyQuery} containing the parameters determining the query. not null.
         * @return false, if no such unit is provided by this provider.
         */
        public boolean isCurrencyAvailable(CurrencyQuery query){
            return !getCurrencies(query).isEmpty();
        }

        @Override
        public Set<CurrencyUnit> getCurrencies(CurrencyQuery query) {
            Set<CurrencyUnit> result = new HashSet<CurrencyUnit>();
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