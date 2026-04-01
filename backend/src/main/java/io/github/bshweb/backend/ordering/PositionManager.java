package io.github.bshweb.backend.ordering;

import org.springframework.stereotype.Component;

@Component
public class PositionManager {

    public static final long MIN_POSITION = 0L;
    public static final long MAX_POSITION = Long.MAX_VALUE;

    public long initialPosition() {
        return midpoint(MIN_POSITION, MAX_POSITION);
    }

    public long newLast(long lastPosition) {
        return midpoint(lastPosition, MAX_POSITION);
    }

    public long newFirst(long firstPosition) {
        return midpoint(MIN_POSITION, firstPosition);
    }

    public long between(long prev, long next) {
        return midpoint(prev, next);
    }

    public boolean hasGapBetween(long prev, long next) {
        return next > prev && next - prev > 1;
    }

    public boolean canAppendAfter(long lastPosition) {
        return hasGapBetween(lastPosition, MAX_POSITION);
    }

    public boolean canPrependBefore(long firstPosition) {
        return hasGapBetween(MIN_POSITION, firstPosition);
    }

    public boolean isValidFirstPosition(long candidate, long firstPosition) {
        return isValidBetweenPosition(candidate, MIN_POSITION, firstPosition);
    }

    public boolean isValidLastPosition(long candidate, long lastPosition) {
        return isValidBetweenPosition(candidate, lastPosition, MAX_POSITION);
    }

    public boolean isValidBetweenPosition(long candidate, long prev, long next) {
        return candidate > prev && candidate < next;
    }

    public long rebalanceValue(long index, long totalCount) {
        if (totalCount <= 0) {
            throw new IllegalArgumentException("totalCount must be positive");
        }

        long step = MAX_POSITION / (totalCount + 1);
        return step * (index + 1);
    }

    private long midpoint(long prev, long next) {
        return prev + (next - prev) / 2; // Instead of the simpler (prev + next) / 2 to avoid possible overflow
    }
}
