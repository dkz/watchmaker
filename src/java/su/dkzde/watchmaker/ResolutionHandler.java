package su.dkzde.watchmaker;

import su.dkzde.watchmaker.core.DirectedAdjusterCondition;
import su.dkzde.watchmaker.core.ScheduleElement;

/**
 * @author Dmitry Kozlov
 */
public interface ResolutionHandler {
    void visitDirectedAdjusterCondition(DirectedAdjusterCondition condition) throws ResolutionException;
    void visitScheduleElement(ScheduleElement element) throws ResolutionException;
    void visitScheduleComponent(Schedule schedule) throws ResolutionException;
}
