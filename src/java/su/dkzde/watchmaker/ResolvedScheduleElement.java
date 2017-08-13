package su.dkzde.watchmaker;

import su.dkzde.watchmaker.core.ScheduleElement;

/**
 * @author Dmitry Kozlov
 */
public class ResolvedScheduleElement implements ResolvedElement {
    private final ScheduleElement element;
    public ResolvedScheduleElement(ScheduleElement element) {
        this.element = element;
    }
    @Override
    public void resolveTo(ResolutionHandler handler) {
        handler.visitScheduleElement(element);
    }
}
