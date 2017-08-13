package su.dkzde.watchmaker;

/**
 * @author Dmitry Kozlov
 */
public class ResolvedScheduleComponent implements ResolvedElement {
    private final Schedule schedule;
    public ResolvedScheduleComponent(Schedule schedule) {
        this.schedule = schedule;
    }
    @Override
    public void resolveTo(ResolutionHandler handler) {
        handler.visitScheduleComponent(schedule);
    }
}
