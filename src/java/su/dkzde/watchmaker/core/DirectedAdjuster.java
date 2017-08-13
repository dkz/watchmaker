package su.dkzde.watchmaker.core;

import java.time.LocalDateTime;
import java.time.temporal.*;
import java.util.*;

/**
 * Magic {@code TemporalAdjuster} which finds the supposedly closer match according to the conditions
 * relative to the provided date in the future or the past.
 * Only {@link ScheduledField}s are supported for conditions.
 * @author Dmitry Kozlov
 */
public final class DirectedAdjuster implements TemporalAdjuster {

    public enum Direction { PAST, FUTURE }

    private final Direction direction;
    private final List<DirectedAdjusterCondition> conditions;

    public static DirectedAdjuster to(Direction direction, DirectedAdjusterCondition... conditions) {
        return to(direction, Arrays.asList(conditions));
    }

    public static DirectedAdjuster to(Direction direction, Collection<DirectedAdjusterCondition> sourceConditions) {
        ArrayList<DirectedAdjusterCondition> conditions = new ArrayList<>(sourceConditions);
        Collections.sort(conditions);
        Collections.reverse(conditions);
        return new DirectedAdjuster(direction, conditions);
    }

    private DirectedAdjuster(Direction direction, List<DirectedAdjusterCondition> conditions) {
        this.direction = direction;
        this.conditions = conditions;

    }

    private boolean isAdjusted(LocalDateTime date) {
        for (DirectedAdjusterCondition condition : conditions) {
            if (date.get(condition.field.getCorrespondingField()) != condition.value) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        LocalDateTime source = LocalDateTime.from(temporal);
        LocalDateTime date = source;
        while (!isAdjusted(date)) {
            Iterator<DirectedAdjusterCondition> condition = conditions.iterator();
            DirectedAdjusterCondition top = condition.next();
            DirectedAdjusterCondition foremost = top;
            LocalDateTime step = date.with(foremost.field.getCorrespondingField(), foremost.value);
            while (condition.hasNext()) {
                DirectedAdjusterCondition current = condition.next();
                step = step.with(current.field.getCorrespondingField(), current.value);
                // When day of week is set, it can knock current date out of the requested month or day,
                // so it's important to find the closest match that correlates to the previous condition field.
                while (step.get(foremost.field.getCorrespondingField()) != foremost.value) {
                    switch (direction) {
                        case FUTURE: {
                            step = step.with(current.field.getFutureAdjuster());
                            break;
                        }
                        case PAST: {
                            step = step.with(current.field.getPastAdjuster());
                            break;
                        }
                    }
                    step = step.with(current.field.getCorrespondingField(), current.value);
                }
                foremost = current;
            }
            // Assuming that previous procedure gets as close as possible to the desired date,
            // if it's still does not match required conditions, then maybe adjuster has to lookup for the date
            // in the next month or year.
            switch (direction) {
                case PAST: {
                    if (step.isAfter(source) || !isAdjusted(step)) {
                        step = step.with(top.field.getPastAdjuster());
                    }
                    break;
                }
                case FUTURE: {
                    if (step.isBefore(source) || !isAdjusted(step)) {
                        step = step.with(top.field.getFutureAdjuster());
                    }
                    break;
                }
            }
            date = step;
        }
        return date;
    }
}
