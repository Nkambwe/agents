package com.pbu.wendi.utils.enums;

public enum AgentType {
    UNKNOWN(0),
    INDIVIDUAL(1),
    BUSINESS(2);

    private final int value;

    AgentType(int value) {
        this.value = value;
    }

    public int getInteger() {
        return value;
    }
    public static AgentType convertToEnum(int value) {
        for (AgentType type : AgentType.values()) {
            if (type.getInteger() == value) {
                return type;
            }
        }
        return AgentType.UNKNOWN;
    }
}
