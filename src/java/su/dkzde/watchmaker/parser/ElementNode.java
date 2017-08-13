package su.dkzde.watchmaker.parser;

import org.parboiled.common.StringUtils;
import su.dkzde.watchmaker.ResolverContext;
import su.dkzde.watchmaker.ResolutionHandler;
import su.dkzde.watchmaker.Schedule;
import su.dkzde.watchmaker.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Kozlov
 */
public final class ElementNode extends ScheduleNode {

    @Override
    public String toString() {
        return "Element {" + StringUtils.join(getChildren(), ",") + "}";
    }

    @Override
    public void resolve(ResolverContext context, ResolutionHandler handler) {
        final List<DirectedAdjusterCondition> conditions = new ArrayList<>();
        final List<ScheduleElement> elements = new ArrayList<>();
        for (ScheduleNode child : getChildren()) {
            child.resolve(context, new ResolutionHandler() {
                @Override
                public void visitDirectedAdjusterCondition(DirectedAdjusterCondition condition) {
                    conditions.add(condition);
                }
                @Override
                public void visitScheduleElement(ScheduleElement element) {
                    elements.add(element);
                }
                @Override
                public void visitScheduleComponent(Schedule schedule) {
                    throw new IllegalArgumentException("Unexpected watchmaker component in watchmaker element");
                }
            });
        }
        if (!conditions.isEmpty()) {
            elements.add(new DirectedAdjusterScheduleElement(conditions));
        }
        if (!elements.isEmpty()) {
            handler.visitScheduleElement(ScheduleElements.group(elements));
        }
    }
}
