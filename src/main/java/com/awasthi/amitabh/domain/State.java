package com.awasthi.amitabh.domain;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum State {
    FIRM(false),
    INDICATIVE(true);

    private boolean isIndicative;

    State(final boolean isIndicative) {
        this.isIndicative = isIndicative;
    }

    public boolean isIndicative() {
        return this.isIndicative;
    }

    /**
     * Do NOT call Enum#values() as it creates a new array-copy each time
     */
    public static final Set<State> VALUES = Collections.unmodifiableSet(EnumSet.allOf(State.class));
}
