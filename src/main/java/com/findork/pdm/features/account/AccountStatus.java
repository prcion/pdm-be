package com.findork.pdm.features.account;

public enum AccountStatus {
    ACTIVE, BANNED, LOCKED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isBanned() {
        return this == BANNED;
    }

    public boolean isLocked() {
        return this == LOCKED;
    }
}
