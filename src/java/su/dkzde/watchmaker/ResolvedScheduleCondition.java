package su.dkzde.watchmaker;

import su.dkzde.watchmaker.core.DirectedAdjusterCondition;
import su.dkzde.watchmaker.core.ScheduledField;

/**
 * @author Dmitry Kozlov
 */
public class ResolvedScheduleCondition implements ResolvedElement {
    private final DirectedAdjusterCondition condition;
    public ResolvedScheduleCondition(DirectedAdjusterCondition condition) {
        this.condition = condition;
    }
    public ResolvedScheduleCondition(ScheduledField field, long value) {
        this.condition = new DirectedAdjusterCondition(field, value);
    }
    @Override
    public void resolveTo(ResolutionHandler handler) {
        handler.visitDirectedAdjusterCondition(condition);
    }
}
