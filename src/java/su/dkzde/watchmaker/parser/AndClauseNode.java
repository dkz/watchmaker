package su.dkzde.watchmaker.parser;

import org.parboiled.common.StringUtils;
import su.dkzde.watchmaker.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Kozlov
 */
public final class AndClauseNode extends ScheduleNode {

    @Override
    public void resolve(ResolverContext context, ResolutionHandler handler) {
        List<ScheduleNode> children = getChildren();
        List<Schedule> resolved = new ArrayList<>(children.size());
        for (ScheduleNode node : children) {
            node.resolve(context, new ScheduleResolutionHandler() {
                @Override
                public void visitSchedule(Schedule schedule) {
                    resolved.add(schedule);
                }
            });
        }
        handler.visitScheduleComponent(Schedules.and(resolved));
    }

    @Override
    public String toString() {
        return "And {" + StringUtils.join(getChildren(), ",") + "}";
    }
}
