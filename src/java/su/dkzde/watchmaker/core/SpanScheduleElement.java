package su.dkzde.watchmaker.core;

import java.time.LocalDateTime;

/**
 * @author Dmitry Kozlov
 */
public final class SpanScheduleElement implements ScheduleElement {

    private final ScheduleElement start;
    private final ScheduleElement end;

    public static SpanScheduleElement create(ScheduleElement start, ScheduleElement end) {
        return new SpanScheduleElement(start, end);
    }

    private SpanScheduleElement(ScheduleElement start, ScheduleElement end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public LocalDateTime adjustToStart(LocalDateTime date) {
        return start.adjustToStart(date);
    }

    @Override
    public LocalDateTime adjustToEnd(LocalDateTime date) {
        return end.adjustToEnd(date);
    }

    @Override
    public ScheduledField resolution() {
        ScheduledField start = this.start.resolution();
        ScheduledField end = this.end.resolution();
        if (start.compareTo(end) < 0) {
            return start;
        } else {
            return end;
        }
    }
}
