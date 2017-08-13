package su.dkzde.watchmaker;

import org.parboiled.errors.ParseError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Kozlov
 */
public class ScheduleParsingException extends RuntimeException {
    private ArrayList<ParseError> errors;
    public ScheduleParsingException(String message, Collection<? extends ParseError> errors) {
        super(message);
        this.errors = new ArrayList<>(errors);
    }
    public ScheduleParsingException(Collection<? extends ParseError> errors) {
        this.errors = new ArrayList<>(errors);
    }
    public List<ParseError> getParseErrors() {
        return Collections.unmodifiableList(errors);
    }
}
