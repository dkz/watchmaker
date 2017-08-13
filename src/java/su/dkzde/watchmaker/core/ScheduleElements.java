package su.dkzde.watchmaker.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Dmitry Kozlov
 */
public final class ScheduleElements {
    private ScheduleElements() {}

    public static ScheduleElement group(Collection<ScheduleElement> elements) {
        return new ScheduleElementGroup(elements);
    }

    private static class ScheduleElementGroup implements ScheduleElement {

        private final ArrayList<ScheduleElement> elements;

        private ScheduleElementGroup(Collection<ScheduleElement> elements) {
            this.elements = new ArrayList<>(elements);
            this.elements.sort(ResolutionComparator.reverse);
        }

        @Override
        public LocalDateTime adjustToStart(LocalDateTime date) {
            LocalDateTime adjusted = date;
            for (ScheduleElement element : elements) {
                adjusted = element.adjustToStart(adjusted);
            }
            return adjusted;
        }

        @Override
        public LocalDateTime adjustToEnd(LocalDateTime date) {
            LocalDateTime adjusted = date;
            Iterator<ScheduleElement> iterator = elements.iterator();
            while (iterator.hasNext()) {
                ScheduleElement element = iterator.next();
                if (iterator.hasNext()) {
                    adjusted = element.adjustToStart(adjusted);
                } else {
                    adjusted = element.adjustToEnd(adjusted);
                }
            }
            return adjusted;
        }

        @Override
        public ScheduledField resolution() {
            return null;
        }
    }
}
