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
package javax.money.convert;


/**
 * A supplier of {@link ExchangeRateProvider} name that references an implementation. This can be used to let
 * an enum type implement this interface, so enums values can be passed to {@link javax.money.convert.MonetaryConversions}
 * for determining the rate providers to be used.
 */
public interface ExchangeRateProviderSupplier{

    /**
     * Get the provider name. This signatire equals to the signrature of java.util.function.Supplier in Java 8.
     *
     * @return the provider name, not null.
     */
    String get();

}
