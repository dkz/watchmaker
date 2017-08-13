package su.dkzde.watchmaker.core;

/**
 * @author Dmitry Kozlov
 */
public final class DirectedAdjusterCondition implements Comparable<DirectedAdjusterCondition> {

    public final ScheduledField field;
    public final long value;

    public DirectedAdjusterCondition(ScheduledField field, long value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public int compareTo(DirectedAdjusterCondition that) {
        return this.field.compareTo(that.field);
    }
}
