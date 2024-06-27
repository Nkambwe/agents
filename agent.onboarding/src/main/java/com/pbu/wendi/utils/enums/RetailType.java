package com.pbu.wendi.utils.enums;

public enum RetailType {
    NA(0),
    STANDARD(1),
    MERCHANT(2);
    private final int value;

    RetailType(int value) {
        this.value = value;
    }

    public int getInteger() {
        return value;
    }

    public static RetailType convertToEnum(int value) {
        for (RetailType type : RetailType.values()) {
            if (type.getInteger() == value) {
                return type;
            }
        }

        return RetailType.NA;
    }
}
