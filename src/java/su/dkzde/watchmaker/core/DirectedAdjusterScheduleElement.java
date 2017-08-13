package su.dkzde.watchmaker.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Kozlov
 */
public class DirectedAdjusterScheduleElement implements ScheduleElement {

    private final List<DirectedAdjusterCondition> conditions;

    public DirectedAdjusterScheduleElement(List<DirectedAdjusterCondition> conditions) {
        this.conditions = new ArrayList<>(conditions);
        Collections.sort(this.conditions);
    }

    private ChronoField getTheMostPreciseField() {
        DirectedAdjusterCondition condition = conditions.get(0);
        return condition.field.getCorrespondingField();
    }

    private LocalDateTime adjustNotSpecifiedFields(LocalDateTime date) {
        LocalDateTime adjusted = date;
        DirectedAdjusterCondition condition = conditions.get(0);
        for (ScheduledField field : ScheduledField.morePreciseThan(condition.field)) {
            adjusted = adjusted.with(field.getCorrespondingField(), 0);
        }
        return adjusted;
    }

    @Override
    public LocalDateTime adjustToStart(LocalDateTime date) {
        return adjustNotSpecifiedFields(date.with(DirectedAdjuster.to(DirectedAdjuster.Direction.PAST, conditions)));
    }

    @Override
    public LocalDateTime adjustToEnd(LocalDateTime date) {
        ChronoField field = getTheMostPreciseField();
        LocalDateTime future = date.with(DirectedAdjuster.to(DirectedAdjuster.Direction.FUTURE, conditions));
        LocalDateTime adjusted = future.with(field, 1 + future.get(field));
        return adjusted;
    }

    @Override
    public ScheduledField resolution() {
        DirectedAdjusterCondition condition = conditions.get(0);
        return condition.field;
    }
}
