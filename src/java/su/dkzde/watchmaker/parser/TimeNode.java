package su.dkzde.watchmaker.parser;

import org.parboiled.support.IndexRange;
import su.dkzde.watchmaker.ResolutionException;
import su.dkzde.watchmaker.ResolverContext;
import su.dkzde.watchmaker.ResolutionHandler;
import su.dkzde.watchmaker.core.DirectedAdjusterCondition;
import su.dkzde.watchmaker.core.ScheduledField;

/**
 * @author Dmitry Kozlov
 */
public final class TimeNode extends ScheduleNode {

    public static final class Builder {

        private NumberNode hours;
        private NumberNode minutes;
        private NumberNode seconds;

        public boolean setHours(NumberNode node) {
            this.hours = node;
            return true;
        }

        public boolean setMinutes(NumberNode node) {
            this.minutes = node;
            return true;
        }

        public boolean setSeconds(NumberNode node) {
            this.seconds = node;
            return true;
        }

        public TimeNode create() {
            return new TimeNode(this);
        }
    }

    private final int hours;
    private final int minutes;
    private final int seconds;

    private final IndexRange hoursRange;
    private final IndexRange minutesRange;
    private final IndexRange secondsRange;

    public TimeNode(Builder builder) {
        this.hours = builder.hours.getValue();
        this.minutes = builder.minutes.getValue();
        this.hoursRange = builder.hours.getRange();
        this.minutesRange = builder.minutes.getRange();
        if (builder.seconds != null) {
            this.seconds = builder.seconds.getValue();
            this.secondsRange = builder.seconds.getRange();
        } else {
            this.seconds = 0;
            this.secondsRange = builder.minutes.getRange();
        }
    }

    @Override
    public void resolve(ResolverContext context, ResolutionHandler handler) {
        try {
            handler.visitDirectedAdjusterCondition(new DirectedAdjusterCondition(ScheduledField.SECOND, seconds));
        } catch (ResolutionException exception) {
            throw new ResolutionException(exception, secondsRange);
        }
        try {
            handler.visitDirectedAdjusterCondition(new DirectedAdjusterCondition(ScheduledField.MINUTE, minutes));
        } catch (ResolutionException exception) {
            throw new ResolutionException(exception, minutesRange);
        }
        try {
            handler.visitDirectedAdjusterCondition(new DirectedAdjusterCondition(ScheduledField.HOUR, hours));
        } catch (ResolutionException exception) {
            throw new ResolutionException(exception, hoursRange);
        }
    }

    @Override
    public String toString() {
        return String.format("TimeNode {%s:%s:%s}", hours, minutes, seconds);
    }
}
