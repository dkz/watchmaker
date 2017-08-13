package su.dkzde.watchmaker.core;

import java.time.LocalDateTime;

/**
 * @author Dmitry Kozlov
 */
public interface ScheduleElement {
    /** @return the closest start of a scheduler interval is the past. */
    LocalDateTime adjustToStart(LocalDateTime date);
    /** @return the closest end of the scheduler interval in the future */
    LocalDateTime adjustToEnd(LocalDateTime date);
    /** @return return the maximum resolution on this watchmaker element */
    ScheduledField resolution();
}
