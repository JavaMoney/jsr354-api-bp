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
package javax.money;

import javax.money.spi.RoundingProviderSpi;
import java.util.HashSet;
import java.util.Set;

public final class TestRoundingProvider implements RoundingProviderSpi {

    @Override
    public MonetaryRounding getRounding(RoundingQuery roundingQuery) {
        if ("foo".equals(roundingQuery.getRoundingName())) {
            return null;
        }
        if (roundingQuery.getRoundingName() != null) {
            return getCustomRounding(roundingQuery.getRoundingName());
        }
        if (roundingQuery.getCurrency() != null) {
            return getCustomRounding(roundingQuery.getCurrency().getCurrencyCode());
        }
        return getCustomRounding("test");
    }

    private MonetaryRounding getCustomRounding(final String customRoundingId) {
        return new MonetaryRounding() {

            private final RoundingContext CTX =
                    RoundingContextBuilder.of("TestRoundingProvider", customRoundingId).build();

            @Override
            public RoundingContext getRoundingContext() {
                return CTX;
            }

            @Override
            public MonetaryAmount apply(MonetaryAmount monetaryAmount) {
                if ("custom1".equals(customRoundingId)) {
                    return monetaryAmount.multiply(2);
                }
                else if ("custom2".equals(customRoundingId)) {
                    return monetaryAmount.multiply(3);
                }
                else{
                    return monetaryAmount;
                }
            }
        };
    }


    @Override
    public Set<String> getRoundingNames() {
        Set<String> result = new HashSet<>();
        result.add("custom1");
        result.add("custom2");
        return result;
    }

    @Override
    public String getProviderName() {
        return getClass().getSimpleName();
    }

}
