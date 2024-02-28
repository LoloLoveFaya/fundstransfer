package com.elf.fundtransfer.infrastructure.util;

import java.util.UUID;

public class UUIDConverter {

    public static Long convertUUIDToLong(UUID uuid) {
        return (long) uuid.hashCode();
    }

    public static UUID convertLongToUUID(Long longValue) {
        return new UUID(longValue, 0);
    }
}
