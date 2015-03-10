/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package javax.money;

import java.math.MathContext;
import java.util.Objects;

/**
 * Amount pseudo (non functional) implementation, for testing only.
 *
 * @author Anatole Tresch
 */
public final class DummyAmount implements MonetaryAmount {

    private static final CurrencyContext DUMMY_CURRENCYCONTEXT = CurrencyContextBuilder.of("dummy").build();

    private final int signum;
    private final MonetaryContext monetaryContext;

    public DummyAmount(final int signum, MonetaryContext monetaryContext) {
        this.signum = signum;
        this.monetaryContext = monetaryContext;
    }

    @Override
    public CurrencyUnit getCurrency() {
        return new CurrencyUnit() {
            @Override
            public String getCurrencyCode() {
                return "DMY";
            }

            @Override
            public int getNumericCode() {
                return 0;
            }

            @Override
            public int getDefaultFractionDigits() {
                return 2;
            }

            @Override
            public CurrencyContext getContext() {
                return DUMMY_CURRENCYCONTEXT;
            }

            @Override
            public int compareTo(CurrencyUnit o) {
                return 0;
            }
        };
    }

    @Override
    public MonetaryContext getContext() {
        return monetaryContext;
    }

    @Override
    public NumberValue getNumber() {
        return new NumberValue() {

            /**
             * serialVersionUID.
             */
            private static final long serialVersionUID = 1L;

            @Override
            public int getPrecision() {
                return 0;
            }

            @Override
            public long getAmountFractionNumerator() {
                return 0;
            }

            @Override
            public long getAmountFractionDenominator() {
                return 0;
            }

            @Override
            public int getScale() {

                return 0;
            }

            @Override
            public int intValue() {
                return 0;
            }

            @Override
            public int intValueExact() {
                return 0;
            }

            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public long longValueExact() {
                return 0;
            }

            @Override
            public double doubleValue() {
                return 0;
            }

            @Override
            public double doubleValueExact() {
                return 0;
            }

            @Override
            public <T extends Number> T numberValue(Class<T> numberType) {
                return null;
            }

            @Override
            public NumberValue round(MathContext mathContext) {
                return this;
            }

            @Override
            public <T extends Number> T numberValueExact(Class<T> numberType) {
                return null;
            }

            @Override
            public float floatValue() {
                return 0;
            }

            @Override
            public Class<?> getNumberType() {
                return Void.class;
            }
        };
    }


    @Override
    public boolean isGreaterThan(MonetaryAmount amount) {

        return false;
    }

    @Override
    public boolean isGreaterThanOrEqualTo(MonetaryAmount amt) {

        return false;
    }

    @Override
    public boolean isLessThan(MonetaryAmount amt) {

        return false;
    }

    @Override
    public boolean isLessThanOrEqualTo(MonetaryAmount amt) {

        return false;
    }

    @Override
    public boolean isEqualTo(MonetaryAmount amount) {

        return false;
    }

    @Override
    public int signum() {

        return signum;
    }

    @Override
    public DummyAmount add(MonetaryAmount amount) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount subtract(MonetaryAmount amount) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount multiply(long multiplicand) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount multiply(double multiplicand) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount multiply(Number multiplicand) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount divide(long amount) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount divide(double amount) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount divide(Number amount) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount remainder(long amount) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount remainder(double amount) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount remainder(Number amount) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount[] divideAndRemainder(long amount) {
        return new DummyAmount[]{new DummyAmountBuilder().create(), new DummyAmountBuilder().create()};
    }

    @Override
    public DummyAmount[] divideAndRemainder(double amount) {
        return new DummyAmount[]{new DummyAmountBuilder().create(), new DummyAmountBuilder().create()};
    }

    @Override
    public DummyAmount[] divideAndRemainder(Number amount) {
        return new DummyAmount[]{new DummyAmountBuilder().create(), new DummyAmountBuilder().create()};
    }

    @Override
    public DummyAmount divideToIntegralValue(long divisor) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount divideToIntegralValue(double divisor) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount divideToIntegralValue(Number divisor) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount scaleByPowerOfTen(int power) {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount abs() {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount negate() {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount plus() {

        return new DummyAmountBuilder().create();
    }

    @Override
    public DummyAmount stripTrailingZeros() {

        return new DummyAmountBuilder().create();
    }

    @Override
    public MonetaryAmountFactory<DummyAmount> getFactory() {
        return new DummyAmountBuilder();
    }

    @Override
    public int compareTo(MonetaryAmount o) {
        Objects.requireNonNull(o);
        return 0;
    }

    /**
     * Queries this monetary amount for a value.
     * <p>
     * This queries this amount using the specified query strategy object.
     * <p>
     * Implementations must ensure that no observable state is altered when this read-only method is
     * invoked.
     *
     * @param <R>   the type of the result
     * @param query the query to invoke, not null
     * @return the query result, null may be returned (defined by the query)
     */
    public <R> R query(MonetaryQuery<R> query){
        return query.queryFrom(this);
    }

    /**
     * Returns an operated object <b>of the same type</b> as this object with the operation made.
     * Hereby returning an instannce <b>of the same type</b> is very important to prevent
     * uncontrolled mixup of implementations. Switching between implementations is still easily
     * possible, e.g. by using according {@link javax.money.MonetaryAmountFactory} instances: <blockquote>
     * <p>
     * <pre>
     * // converting from Money to MyMoney
     * Money m = ...;
     * MonetartyAmountFactory<MyMoney> f = MonetaryAmounts.queryAmountFactory(MyMoney.class);
     * MyMoney myMoney = f.setAmount(m).of();
     * </blockquote>
     * </pre>
     * <p>
     * This converts this monetary amount according to the rules of the specified operator. A
     * typical operator will change the amount and leave the currency unchanged. A more complex
     * operator might also change the currency.
     * <p>
     * Some example code indicating how and why this method is used:
     * <p>
     * <blockquote>
     * <p>
     * <pre>
     * MonetaryAmount money = money.with(amountMultipliedBy(2));
     * money = money.with(amountRoundedToNearestWholeUnit());
     * </pre>
     * <p>
     * </blockquote>
     * <p>
     * Hereby also the method signature on the implementation type must return the concrete type, to
     * enable a fluent API, e.g.
     * <p>
     * <blockquote>
     * <p>
     * <pre>
     * public final class MyMoney implements MonetaryAmount{
     *   ...
     *   public MyMoney with(MonetaryOperator operator){
     *     ...
     *   }
     *
     *   ...
     * }
     * </pre>
     * <p>
     * </blockquote>
     *
     * @param operator the operator to use, not null
     * @return an object of the same type with the specified conversion made, not null
     */
    public MonetaryAmount with(MonetaryOperator operator){
        return operator.apply(this);
    }


    /**
     * Checks if a {@code MonetaryAmount} is negative.
     *
     * @return {@code true} if {@link #signum()} < 0.
     */
    public boolean isNegative(){
        return signum() < 0;
    }

    /**
     * Checks if a {@code MonetaryAmount} is negative or zero.
     *
     * @return {@code true} if {@link #signum()} <= 0.
     */
    public boolean isNegativeOrZero(){
        return signum() <= 0;
    }

    /**
     * Checks if a {@code MonetaryAmount} is positive.
     *
     * @return {@code true} if {@link #signum()} > 0.
     */
    public boolean isPositive(){
        return signum() > 0;
    }

    /**
     * Checks if a {@code MonetaryAmount} is positive or zero.
     *
     * @return {@code true} if {@link #signum()} >= 0.
     */
    public boolean isPositiveOrZero(){
        return signum() >= 0;
    }

    /**
     * Checks if an {@code MonetaryAmount} is zero.
     *
     * @return {@code true} if {@link #signum()} == 0.
     */
    public boolean isZero(){
        return signum() == 0;
    }
}
