package su.dkzde.watchmaker.parser;

import su.dkzde.watchmaker.ResolverContext;
import su.dkzde.watchmaker.ResolutionHandler;
import su.dkzde.watchmaker.Schedule;
import su.dkzde.watchmaker.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Kozlov
 */
public final class SpanNode extends ScheduleNode {

    private ScheduleNode from;
    private ScheduleNode till;

    public boolean setFrom(ScheduleNode node) {
        addChild(node);
        this.from = node;
        return true;
    }

    public boolean setTill(ScheduleNode node) {
        addChild(node);
        this.till = node;
        return true;
    }

    @Override
    public void resolve(ResolverContext context, ResolutionHandler handler) {
        ScheduleElement from = resolve(context, this.from);
        ScheduleElement till = resolve(context, this.till);
        handler.visitScheduleElement(SpanScheduleElement.create(from, till));
    }

    private ScheduleElement resolve(ResolverContext context, ScheduleNode node) {
        final List<DirectedAdjusterCondition> conditions = new ArrayList<>();
        final List<ScheduleElement> elements = new ArrayList<>();
        node.resolve(context, new ResolutionHandler() {
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
                throw new IllegalStateException("Unexpected watchmaker component");
            }
        });
        if (!conditions.isEmpty()) {
            elements.add(new DirectedAdjusterScheduleElement(conditions));
        }
        return ScheduleElements.group(elements);
    }

    @Override
    public ScheduleNode simplify() {
        from = from.simplify();
        till = till.simplify();
        return this;
    }

    @Override
    public String toString() {
        return "Span: (" + from.toString() + " - " + till.toString() + ")";
    }
}
