package com.pbu.wendi.utils.enums;

public enum AgentCategory {
    //Unknown agent category
    UNKNOWN(0),
    //Retail agent
    RETAIL(1),
    //super agent
    SUPER(2);
    private final int value;

    AgentCategory(int value) {
        this.value = value;
    }

    public int getInteger() {
        return value;
    }
    public static AgentCategory convertToEnum(int value) {
        for (AgentCategory type : AgentCategory.values()) {
            if (type.getInteger() == value) {
                return type;
            }
        }
        return AgentCategory.UNKNOWN;
    }
}

