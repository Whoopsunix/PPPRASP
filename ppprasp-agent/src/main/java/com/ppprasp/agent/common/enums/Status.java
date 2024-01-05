package com.ppprasp.agent.common.enums;

/**
 * @author Whoopsunix
 *
 * 算法状态
 */
public enum Status {
    OPEN(1, "open"),
    CLOSE(0, "close"),
    LOG(-1, "log"),
    ;

    private final int value;
    private final String description;

    Status(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}