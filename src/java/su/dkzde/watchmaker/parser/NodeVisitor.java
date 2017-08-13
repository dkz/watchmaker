package su.dkzde.watchmaker.parser;

import su.dkzde.watchmaker.ResolverContext;
import su.dkzde.watchmaker.ResolutionHandler;

/**
 * @author Dmitry Kozlov
 */
public interface NodeVisitor {
    void visit(ResolverContext context, ScheduleNode node, ResolutionHandler handler);
}
