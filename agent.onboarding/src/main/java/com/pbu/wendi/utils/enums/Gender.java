package com.pbu.wendi.utils.enums;


public enum Gender {
    UNKNOWN(0),
    Male(1),
    Female(2);
    private final int value;

    Gender(int value) {
        this.value = value;
    }

    public int getInteger() {
        return value;
    }

    public static Gender convertToEnum(int value) {
        for (Gender type : Gender.values()) {
            if (type.getInteger() == value) {
                return type;
            }
        }

        return Gender.UNKNOWN;
    }
}