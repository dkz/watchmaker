package su.dkzde.watchmaker.core;

import java.util.Comparator;

/**
 * @author Dmitry Kozlov
 */
public class ResolutionComparator implements Comparator<ScheduleElement> {

    public static final ResolutionComparator natural = new ResolutionComparator(1);
    public static final ResolutionComparator reverse = new ResolutionComparator(-1);

    private final int order;

    private ResolutionComparator(int order) {
        this.order = order;
    }

    @Override
    public int compare(ScheduleElement o1, ScheduleElement o2) {
        ScheduledField f1 = o1.resolution();
        ScheduledField f2 = o2.resolution();
        return order * f1.compareTo(f2);
    }
}
