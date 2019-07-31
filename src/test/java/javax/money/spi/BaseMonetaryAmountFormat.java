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
import javax.money.format.MonetaryAmountFormat;
import java.io.IOException;

/**
 * <p>
 * Formats instances of {@code MonetaryAmount} to a {@link String} or an {@link Appendable}.
 * </p>
 * <p>
 * To obtain a <code>MonetaryAmountFormat</code> for a specific locale, including the default
 * locale, call {@link javax.money.format.MonetaryFormats#getAmountFormat(java.util.Locale, String...)}.
 *
 * More complex formatting scenarios can be implemented by registering instances of {@link javax.money.spi.MonetaryAmountFormatProviderSpi}.
 * The spi implementation creates new instances of {@link BaseMonetaryAmountFormat} based on the
 * <i>styleId</i> and <i> (arbitrary) attributes</i> passed within the {@link javax.money.format.AmountFormatContext}.
 * </p>
 * <p>In general, do prefer
 * accessing <code>MonetaryAmountFormat</code> instances from the {@link javax.money.format.MonetaryFormats} singleton,
 * instead of instantiating implementations directly, since the <code>MonetaryFormats</code> factory
 * method may return different subclasses or may implement contextual behaviour (in a EE context).
 * If you need to customize the format object, do something like this:
 * <p>
 * <blockquote>
 * <p>
 * <pre>
 * MonetaryAmountFormat f = MonetaryFormats.getInstance(loc);
 * f.setStyle(f.getStyle().toBuilder().setPattern(&quot;###.##;(###.##)&quot;).build());
 * </pre>
 * <p>
 * </blockquote>
 * <p>
 * <b>Special Values</b>
 * <p>
 * <p>
 * Negative zero (<code>"-0"</code>) should always parse to
 * <ul>
 * <li><code>0</code></li>
 * </ul>
 * <p>
 * <b><a name="synchronization">Synchronization</a></b>
 * <p>
 * <p>
 * Instances of this class are not required to be thread-safe. It is recommended to of separate
 * format instances for each thread. If multiple threads access a format concurrently, it must be
 * synchronized externally.
 * <p>
 * <b>Example</b>
 * <p>
 * <blockquote>
 * <p>
 * <pre>
 * <strong>// Print out a number using the localized number, currency,
 * // for each locale</strong>
 * Locale[] locales = MonetaryFormats.getAvailableLocales();
 * MonetaryAmount amount = ...;
 * MonetaryAmountFormat form;
 *     System.out.println("FORMAT");
 *     for (int i = 0; i < locales.length; ++i) {
 *         if (locales[i].getCountry().length() == 0) {
 *            continue; // Skip language-only locales
 *         }
 *         System.out.print(locales[i].getDisplayName());
 *         form = MonetaryFormats.getInstance(locales[i]);
 *         System.out.print(": " + form.getStyle().getPattern());
 *         String myAmount = form.format(amount);
 *         System.out.print(" -> " + myAmount);
 *         try {
 *             System.out.println(" -> " + form.parse(form.format(myAmount)));
 *         } catch (ParseException e) {}
 *     }
 * }
 * </pre>
 * <p>
 * </blockquote>
 */
public abstract class BaseMonetaryAmountFormat implements MonetaryAmountFormat {

    /**
     * Formats the given {@link javax.money.MonetaryAmount} to a String.
     *
     * @param amount the amount to format, not {@code null}
     * @return the string printed using the settings of this formatter
     * @throws UnsupportedOperationException if the formatter is unable to print
     */
    public String format(MonetaryAmount amount){
        StringBuilder b = new StringBuilder();
        try{
            print(b, amount);
        }
        catch(IOException e){
            throw new IllegalStateException("Formatting error.", e);
        }
        return b.toString();
    }

}
