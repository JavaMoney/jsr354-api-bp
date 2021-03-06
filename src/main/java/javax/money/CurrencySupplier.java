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
package javax.money;


/**
 * Represents a supplier of {@link CurrencyUnit}-valued results. This is the
 * {@link CurrencyUnit}-producing specialization of {@code Supplier} (as in Java 8).
 * 
 * <p>
 * There is no requirement that a distinct result be returned each time the
 * supplier is invoked.
 * 
 * <p>
 * This is a <b>functional interface</b> whose
 * functional method is {@link #getCurrency()}.
 * This class does not extends {@code java.util.function.Supplier} since {@link MonetaryAmount} implements
 * both supplier interfaces, {@link NumberSupplier} and {@link CurrencySupplier},
 * which will lead
 * to method name conflicts.
 * </p>
 *
 * @author Werner Keil
 * @version 1.0
 * @since 0.8
 */
//@FunctionalInterface
public interface CurrencySupplier {

	/**
	 * Gets the corresponding {@link CurrencyUnit}.
	 * 
	 * @return the corresponding {@link CurrencyUnit}, not null.
	 */
	CurrencyUnit getCurrency();
}