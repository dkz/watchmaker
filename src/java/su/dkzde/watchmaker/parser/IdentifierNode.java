package su.dkzde.watchmaker.parser;

import org.parboiled.support.IndexRange;
import su.dkzde.watchmaker.ResolutionException;
import su.dkzde.watchmaker.ResolverContext;
import su.dkzde.watchmaker.ResolutionHandler;

/**
 * @author Dmitry Kozlov
 */
public final class IdentifierNode extends ScheduleNode {

    private final String id;
    private final IndexRange range;

    public IdentifierNode(String id, IndexRange range) {
        this.id = id;
        this.range = range;
    }

    @Override
    public String toString() {
        return "Identifier " + id;
    }

    @Override
    public void resolve(ResolverContext context, ResolutionHandler handler) {
        try {
            context.namespace.resolve(id, handler);
        } catch (ResolutionException exception) {
            throw new ResolutionException(exception, range);
        }
    }
}
