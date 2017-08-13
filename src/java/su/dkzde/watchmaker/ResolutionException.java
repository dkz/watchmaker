package su.dkzde.watchmaker;

import org.parboiled.support.IndexRange;

/**
 * @author Dmitry Kozlov
 */
public class ResolutionException extends RuntimeException {
    private IndexRange range;
    public ResolutionException(String message) {
        super(message);
    }
    public ResolutionException(String message, IndexRange range) {
        super(message);
        this.range = range;
    }
    public ResolutionException(ResolutionException exception, IndexRange range) {
        super(exception.getMessage(), exception.getCause());
        this.range = range;
    }
}
