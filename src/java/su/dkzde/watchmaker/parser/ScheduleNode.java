package su.dkzde.watchmaker.parser;

import su.dkzde.watchmaker.ResolverContext;
import su.dkzde.watchmaker.ResolutionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Kozlov
 */
public abstract class ScheduleNode {

    private List<ScheduleNode> children = new ArrayList<>(0);

    public List<ScheduleNode> getChildren() {
        return new ArrayList<>(children);
    }

    public ScheduleNode addChild(ScheduleNode node) {
        children.add(node);
        return this;
    }

    public ScheduleNode removeChild(ScheduleNode node) {
        children.remove(node);
        return this;
    }

    public abstract void resolve(ResolverContext context, ResolutionHandler handler);

    public boolean isTerminal() {
        return children.isEmpty();
    }

    public ScheduleNode simplify() {
        if (this.isTerminal()) {
            return this;
        }
        List<ScheduleNode> children = getChildren();
        if (children.size() > 1) {
            for (ScheduleNode node : children) {
                this.removeChild(node);
                this.addChild(node.simplify());
            }
            return this;
        } else {
            return children.get(0).simplify();
        }
    }
}
