package su.dkzde.watchmaker.parser;

import org.parboiled.common.StringUtils;
import su.dkzde.watchmaker.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Kozlov
 */
public final class OrClauseNode extends ScheduleNode {

    @Override
    public void resolve(ResolverContext context, ResolutionHandler handler) {
        final List<ScheduleNode> children = getChildren();
        final List<Schedule> resolved = new ArrayList<>(children.size());
        for (ScheduleNode node : children) {
            node.resolve(context, new ScheduleResolutionHandler() {
                @Override
                public void visitSchedule(Schedule schedule) {
                    resolved.add(schedule);
                }
            });
        }
        handler.visitScheduleComponent(Schedules.or(resolved));
    }

    @Override
    public String toString() {
        return "Or {" + StringUtils.join(getChildren(), ",") + "}";
    }
}
