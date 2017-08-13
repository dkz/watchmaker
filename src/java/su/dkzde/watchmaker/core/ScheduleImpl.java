package su.dkzde.watchmaker.core;

import su.dkzde.watchmaker.Schedule;

import java.time.LocalDateTime;

/**
 * @author Dmitry Kozlov
 */
public final class ScheduleImpl implements Schedule {

    private final ScheduleElement element;

    public ScheduleImpl(ScheduleElement element) {
        this.element = element;
    }

    @Override
    public boolean isSatisfied(LocalDateTime date) {
        LocalDateTime start = element.adjustToStart(date);
        LocalDateTime end = element.adjustToEnd(start);
        return date.isAfter(start)
            && date.isBefore(end);
    }
}
