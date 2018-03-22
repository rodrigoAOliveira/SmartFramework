package com.arcthos.arcthosmart.smartorm;

/**
 * Created by Vinicius Damiati on 03-Oct-17.
 */

public class Condition {
    private String property;
    private Object value;
    private Check check;

    public Condition(String property) {
        this.property = property;
    }

    public static Condition prop(String property) {
        return new Condition(property);
    }

    public Condition eq(Object value) {
        if (value == null) {
            return isNull();
        }
        setValue(value);
        check = Check.EQUALS;
        return this;
    }

    public Condition like(Object value) {
        setValue(value);
        check = Check.LIKE;
        return this;
    }

    public Condition likePrefix(Object value) {
        setValue("%" + value);
        check = Check.LIKE;
        return this;
    }

    public Condition likeSufix(Object value) {
        setValue(value + "%");
        check = Check.LIKE;
        return this;
    }

    public Condition likeAll(Object value) {
        setValue("%" + value + "%");
        check = Check.LIKE;
        return this;
    }

    public Condition notLike(Object value) {
        setValue(value);
        check = Check.NOT_LIKE;
        return this;
    }

    public Condition notLikePrefix(Object value) {
        setValue("%" + value);
        check = Check.NOT_LIKE;
        return this;
    }

    public Condition notLikeSufix(Object value) {
        setValue(value + "%");
        check = Check.NOT_LIKE;
        return this;
    }

    public Condition notLikeAll(Object value) {
        setValue("%" + value + "%");
        check = Check.NOT_LIKE;
        return this;
    }

    public Condition notEq(Object value) {
        if (value == null) {
            return isNotNull();
        }
        setValue(value);
        check = Check.NOT_EQUALS;
        return this;
    }

    public Condition gt(Object value) {
        setValue(value);
        check = Check.GREATER_THAN;
        return this;
    }

    public Condition lt(Object value) {
        setValue(value);
        check = Check.LESSER_THAN;
        return this;
    }

    public Condition gteq(Object value) {
        setValue(value);
        check = Check.GREATER_THAN_EQUAL;
        return this;
    }

    public Condition lteq(Object value) {
        setValue(value);
        check = Check.LESSER_THAN_EQUAL;
        return this;
    }

    public Condition isNull() {
        setValue(null);
        check = Check.IS_NULL;
        return this;
    }

    public Condition isNotNull() {
        setValue(null);
        check = Check.IS_NOT_NULL;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public Object getValue() {
        return value;
    }

    private void setValue(Object value) {
        this.value = value;
    }

    public Check getCheck() {
        return check;
    }

    public String getCheckSymbol() {
        return check.getSymbol();
    }

    enum Check {
        EQUALS(" = "),
        GREATER_THAN(" > "),
        LESSER_THAN(" < "),
        GREATER_THAN_EQUAL(" >= "),
        LESSER_THAN_EQUAL(" <= "),
        NOT_EQUALS(" != "),
        LIKE(" LIKE "),
        NOT_LIKE(" NOT LIKE "),
        IS_NULL(" IS NULL "),
        IS_NOT_NULL(" IS NOT NULL ");

        private String symbol;

        Check(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    enum Type {
        AND,
        OR,
        NOT
    }
}
