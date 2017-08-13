package su.dkzde.watchmaker;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import su.dkzde.watchmaker.parser.ScheduleGrammar;
import su.dkzde.watchmaker.parser.ScheduleNode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Dmitry Kozlov
 */
public final class Schedules {
    private Schedules() {}

    private static final ScheduleGrammar parser = Parboiled.createParser(ScheduleGrammar.class);

    public static Schedule resolve(String schedule, ResolverContext context) throws ScheduleParsingException, ResolutionException {
        ParsingResult<ScheduleNode> result = new ReportingParseRunner<ScheduleNode>(parser.Schedule()).run(schedule);
        if (!result.matched) {
            throw new ScheduleParsingException(result.parseErrors);
        } else {
            final ThreadLocal<Schedule> reference = new ThreadLocal<>();
            ScheduleNode root = result.resultValue.simplify();
            root.resolve(context, new ScheduleResolutionHandler() {
                @Override
                public void visitSchedule(Schedule schedule) {
                    reference.set(schedule);
                }
            });
            return reference.get();
        }
    }

    public static Schedule year(int year) {
        return new Year(year);
    }

    public static Schedule not(Schedule schedule) {
        return new Not(schedule);
    }

    public static Schedule and(Collection<Schedule> schedules) {
        return new And(schedules);
    }

    public static Schedule and(Schedule... schedules) {
        return and(Arrays.asList(schedules));
    }

    public static Schedule or(Collection<Schedule> schedules) {
        return new Or(schedules);
    }

    public static Schedule or(Schedule... schedules) {
        return or(Arrays.asList(schedules));
    }

    private static final class Or implements Schedule {
        private final ArrayList<Schedule> or;
        private Or(Collection<Schedule> or) {
            this.or = new ArrayList<>(or);
        }
        @Override
        public boolean isSatisfied(LocalDateTime date) {
            for (Schedule schedule : or) {
                if (schedule.isSatisfied(date)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final class And implements Schedule {
        private final ArrayList<Schedule> and;
        private And(Collection<Schedule> and) {
            this.and = new ArrayList<>(and);
        }
        @Override
        public boolean isSatisfied(LocalDateTime date) {
            for (Schedule schedule : and) {
                if (!schedule.isSatisfied(date)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static final class Not implements Schedule {
        private final Schedule schedule;
        private Not(Schedule schedule) {
            this.schedule = schedule;
        }
        @Override
        public boolean isSatisfied(LocalDateTime date) {
            return !schedule.isSatisfied(date);
        }
    }

    private static final class Year implements Schedule {
        private final int year;
        private Year(int year) {
            this.year = year;
        }
        @Override
        public boolean isSatisfied(LocalDateTime date) {
            return year == date.get(ChronoField.YEAR);
        }
    }
}
