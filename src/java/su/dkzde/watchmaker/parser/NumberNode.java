package su.dkzde.watchmaker.parser;

import org.parboiled.support.IndexRange;
import su.dkzde.watchmaker.ResolutionException;
import su.dkzde.watchmaker.ResolverContext;
import su.dkzde.watchmaker.ResolutionHandler;

/**
 * @author Dmitry Kozlov
 */
public final class NumberNode extends ScheduleNode {

    private final int number;
    private final IndexRange range;

    public NumberNode(int number, IndexRange range) {
        this.number = number;
        this.range = range;
    }

    public int getValue() {
        return number;
    }

    public IndexRange getRange() {
        return range;
    }

    @Override
    public String toString() {
        return "Number " + number;
    }

    @Override
    public void resolve(ResolverContext context, ResolutionHandler handler) {
        try {
            context.numbers.resolve(number, handler);
        } catch (ResolutionException exception) {
            throw new ResolutionException(exception, range);
        }
    }
}
