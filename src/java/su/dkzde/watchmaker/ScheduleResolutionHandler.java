package su.dkzde.watchmaker;

import su.dkzde.watchmaker.core.*;

import java.util.Collections;

/**
 * @author Dmitry Kozlov
 */
public abstract class ScheduleResolutionHandler implements ResolutionHandler {

    public abstract void visitSchedule(Schedule schedule);

    @Override
    public void visitDirectedAdjusterCondition(DirectedAdjusterCondition condition) {
        visitScheduleElement(new DirectedAdjusterScheduleElement(Collections.singletonList(condition)));
    }

    @Override
    public void visitScheduleElement(ScheduleElement element) {
        visitScheduleComponent(new ScheduleImpl(element));
    }

    @Override
    public void visitScheduleComponent(Schedule schedule) {
        visitSchedule(schedule);
    }
}
