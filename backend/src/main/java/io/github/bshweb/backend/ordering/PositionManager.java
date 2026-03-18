package io.github.bshweb.backend.ordering;

import org.springframework.stereotype.Component;

@Component
public class PositionManager {

    public static final long POSITION_STEP = 65536L;

    public long initialPosition() {
        return POSITION_STEP;
    }

    public long newLast(long lastPosition) {
        return Math.addExact(lastPosition, POSITION_STEP);
    }

    public long newFirst(long firstPosition) {
        return firstPosition / 2;
    }

    public long between(long prev, long next) {
        return prev + (next - prev) / 2;    // Instead of the simpler (prev + next) / 2 to avoid possible overflow
    }

    public boolean hasGapBetween(long prev, long next) {
        return next - prev > 1;
    }

    public boolean canAppendAfter(long lastPosition) {
        return lastPosition <= Long.MAX_VALUE - POSITION_STEP;
    }

    public boolean isValidFirstPosition(long candidate, long firstPosition) {
        return candidate >= 0 && candidate < firstPosition;
    }

    public boolean isValidBetweenPosition(long candidate, long prev, long next) {
        return candidate > prev && candidate < next;
    }
}
