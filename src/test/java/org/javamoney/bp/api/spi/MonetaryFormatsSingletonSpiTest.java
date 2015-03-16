/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.api.spi;

import org.javamoney.bp.api.DummyAmountBuilder;
import org.javamoney.bp.api.MonetaryAmount;
import org.javamoney.bp.api.MonetaryException;
import org.javamoney.bp.api.TestCurrency;
import org.javamoney.bp.api.format.AmountFormatContext;
import org.javamoney.bp.api.format.AmountFormatContextBuilder;
import org.javamoney.bp.api.format.AmountFormatQuery;
import org.javamoney.bp.api.format.AmountFormatQueryBuilder;
import org.javamoney.bp.api.format.MonetaryAmountFormat;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static org.testng.AssertJUnit.*;

/**
 * Tests the default methods on MonetaryAmountsSingletonSpi.
 */
public class MonetaryFormatsSingletonSpiTest {

    private MonetaryFormatsSingletonSpi testSpi = new BaseMonetaryFormatsSingletonSpi() {

        @Override
        public Set<Locale> getAvailableLocales(String... providers) {
            Set<Locale> locales = new HashSet<Locale>();
            locales.add(Locale.ENGLISH);
            return locales;
        }

        @Override
        public Collection<MonetaryAmountFormat> getAmountFormats(AmountFormatQuery formatQuery) {
            List<MonetaryAmountFormat> formats = new ArrayList<MonetaryAmountFormat>();
            if (Locale.ENGLISH.equals(formatQuery.getLocale())) {
                formats.add(new PseudoFormat());
            } else if ("f1".equals(formatQuery.getFormatName())) {
                formats.add(new PseudoFormat());
            }
            return formats;
        }

        @Override
        public Set<String> getProviderNames() {
            Set<String> names = new HashSet<String>();
            names.add("b");
            return names;
        }

        @Override
        public List<String> getDefaultProviderChain() {
            List<String> names = new ArrayList<String>();
            names.add("b");
            return names;
        }

        class PseudoFormat extends BaseMonetaryAmountFormat {

            @Override
            public AmountFormatContext getContext() {
                return AmountFormatContextBuilder.of(Locale.ENGLISH).build();
            }

            @Override
            public void print(Appendable appendable, MonetaryAmount amount) throws IOException {
                appendable.append("<PseudoFormat>");
            }

            @Override
            public MonetaryAmount parse(CharSequence text) {
                return new DummyAmountBuilder().setCurrency(TestCurrency.of("CHF")).setNumber(10.5).create();
            }

            @Override
            public String queryFrom(MonetaryAmount amount) {
                return "<PseudoFormat>";
            }
        }

    };


    @Test
    public void testGetAmountFormat_Query() {
        MonetaryAmountFormat f = testSpi.getAmountFormat(AmountFormatQueryBuilder.of(Locale.ENGLISH).build());
        assertNotNull(f);
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetAmountFormat_Query_BC() {
        testSpi.getAmountFormat(AmountFormatQueryBuilder.of(Locale.CHINESE).build());
    }


    @Test
    public void testIsAvailable_Query() {
        assertTrue(testSpi.isAvailable(AmountFormatQueryBuilder.of(Locale.ENGLISH).build()));
        assertFalse(testSpi.isAvailable(AmountFormatQueryBuilder.of(Locale.CHINESE).build()));
    }

    @Test
    public void testIsAvailable_Locale_StringArr() {
        assertTrue(testSpi.isAvailable(Locale.ENGLISH));
        assertFalse(testSpi.isAvailable(Locale.CHINESE));
        assertTrue(testSpi.isAvailable(Locale.ENGLISH, "b"));
        assertFalse(testSpi.isAvailable(Locale.CHINESE, "b"));
        assertTrue(testSpi.isAvailable(Locale.ENGLISH, "dsdsd"));
        assertFalse(testSpi.isAvailable(Locale.CHINESE, "sdsd"));
    }

    @Test
    public void testGetAmountFormat_Locale_StringArr() {
        assertNotNull(testSpi.getAmountFormat(Locale.ENGLISH));
        assertNotNull(testSpi.getAmountFormat(Locale.ENGLISH, "b"));
        assertNotNull(testSpi.getAmountFormat(Locale.ENGLISH, "dsdsd"));
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetAmountFormat_Locale_StringArr_BC1() {
        testSpi.getAmountFormat(Locale.CHINESE);
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetAmountFormat_Locale_StringArr_BC2() {
        testSpi.getAmountFormat(Locale.CHINESE, "b");
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetAmountFormat_Locale_StringArr_BC3() {
        testSpi.getAmountFormat(Locale.CHINESE, "sdsd");
    }

    @Test
    public void testGetAmountFormat_String_StringArr() {
        assertNotNull(testSpi.getAmountFormat("f1"));
        assertNotNull(testSpi.getAmountFormat("f1", "b"));
        assertNotNull(testSpi.getAmountFormat("f1", "dsdsd"));
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetAmountFormat_String_StringArr_BC1() {
        assertNull(testSpi.getAmountFormat("foo"));
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetAmountFormat_String_StringArr_BC2() {
        assertNull(testSpi.getAmountFormat("foo", "b"));
    }

    @Test(expectedExceptions = {MonetaryException.class})
    public void testGetAmountFormat_String_StringArr_BC3() {
        assertNull(testSpi.getAmountFormat("foo", "sdsd"));
    }

}
