package com.acm.security;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/** Minimal in-memory limiter (single instance, fine for one jar on one VPS). */
public class AttemptLimiter {

    private record Entry(int count, Instant windowEnd) {}

    private static final int SOFT_CAP = 10_000;
    private static final int HARD_CAP = 50_000;

    private final int max;
    private final Duration window;
    private final ConcurrentHashMap<String, Entry> entries = new ConcurrentHashMap<>();

    public AttemptLimiter(int max, Duration window) {
        this.max = max;
        this.window = window;
    }

    public boolean isBlocked(String key) {
        Entry e = entries.get(key);
        return e != null && e.windowEnd().isAfter(Instant.now()) && e.count() >= max;
    }

    public void record(String key) {
        Instant now = Instant.now();
        if (entries.size() > SOFT_CAP) {
            entries.values().removeIf(e -> e.windowEnd().isBefore(now));
            if (entries.size() > HARD_CAP) entries.clear();   // memory protection
        }
        entries.merge(key, new Entry(1, now.plus(window)),
                (old, fresh) -> old.windowEnd().isBefore(now)
                        ? fresh
                        : new Entry(old.count() + 1, old.windowEnd()));
    }

    public void reset(String key) {
        entries.remove(key);
    }
}