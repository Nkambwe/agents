package com.pbu.wendi.utils.enums;

public enum Status {
    UNKNOWN(0),
    PENDING(1),
    APPROVED(2),
    REJECTED(3);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public int getInteger() {
        return value;
    }
    public static Status convertToEnum(int value) {
        for (Status type : Status.values()) {
            if (type.getInteger() == value) {
                return type;
            }
        }
        return Status.UNKNOWN;
    }
}