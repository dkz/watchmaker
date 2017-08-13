package su.dkzde.watchmaker.parser;

import su.dkzde.watchmaker.*;

/**
 * @author Dmitry Kozlov
 */
public class NotNode extends ScheduleNode {

    private final ScheduleNode node;

    public NotNode(ScheduleNode node) {
        this.node = node;
    }

    @Override
    public void resolve(ResolverContext context, ResolutionHandler handler) {
        final ThreadLocal<Schedule> resolved = new ThreadLocal<>();
        node.resolve(context, new ScheduleResolutionHandler() {
            @Override
            public void visitSchedule(Schedule schedule) {
                resolved.set(schedule);
            }
        });
        handler.visitScheduleComponent(Schedules.not(resolved.get()));
    }

    @Override
    public String toString() {
        return "Not {" + node.toString() + "}";
    }
}
