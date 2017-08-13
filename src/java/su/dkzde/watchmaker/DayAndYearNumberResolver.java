package su.dkzde.watchmaker;

import su.dkzde.watchmaker.core.DirectedAdjusterCondition;
import su.dkzde.watchmaker.core.ScheduledField;

/**
 * @author Dmitry Kozlov
 */
public class DayAndYearNumberResolver implements NumberResolver {
    @Override
    public void resolve(int number, ResolutionHandler handler) {
        if (number < 0) {
            throw new IllegalArgumentException("Unknown identifier '" + number + "'");
        }
        if (number < 32) {
            handler.visitDirectedAdjusterCondition(new DirectedAdjusterCondition(ScheduledField.DAY, number));
        } else {
            handler.visitScheduleComponent(Schedules.year(number));
        }
    }
}
