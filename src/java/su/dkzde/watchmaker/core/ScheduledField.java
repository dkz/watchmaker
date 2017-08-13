package su.dkzde.watchmaker.core;

import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

/**
 * Core supporting class that describes which {@code ChronoField}s can be scheduled.
 * @author Dmitry Kozlov
 */
public enum ScheduledField {

    // Order of enum definitions are important: condition building uses natural order of this values,
    // using less specific fields for search first.

    MILLISECOND(ChronoField.MILLI_OF_SECOND, TopLevelAdjusters.of(ChronoUnit.SECONDS)),
    SECOND(ChronoField.SECOND_OF_MINUTE, TopLevelAdjusters.of(ChronoUnit.MINUTES)),
    MINUTE(ChronoField.MINUTE_OF_HOUR, TopLevelAdjusters.of(ChronoUnit.HOURS)),
    HOUR(ChronoField.HOUR_OF_DAY, TopLevelAdjusters.of(ChronoUnit.DAYS)),

    WEEKDAY(ChronoField.DAY_OF_WEEK, new TopLevelAdjusters(
            TemporalAdjusters.previous(DayOfWeek.SUNDAY),
            TemporalAdjusters.next(DayOfWeek.MONDAY))),

    DAY(ChronoField.DAY_OF_MONTH, TopLevelAdjusters.of(ChronoUnit.MONTHS)),
    MONTH(ChronoField.MONTH_OF_YEAR, TopLevelAdjusters.of(ChronoUnit.YEARS));

    private static class TopLevelAdjusters {
        private final TemporalAdjuster past;
        private final TemporalAdjuster future;
        private TopLevelAdjusters(TemporalAdjuster past, TemporalAdjuster future) {
            this.past = past;
            this.future = future;
        }
        private static TopLevelAdjusters of(ChronoUnit unit) {
            return new TopLevelAdjusters(
                    temporal -> temporal.plus(-1, unit),
                    temporal -> temporal.plus(1, unit));
        }
    }

    private final ChronoField chronoField;
    private final TopLevelAdjusters topLevelAdjusters;
    ScheduledField(ChronoField chronoField, TopLevelAdjusters topLevelAdjusters) {
        this.chronoField = chronoField;
        this.topLevelAdjusters = topLevelAdjusters;
    }

    public ChronoField getCorrespondingField() {
        return chronoField;
    }

    public TemporalAdjuster getFutureAdjuster() {
        return topLevelAdjusters.future;
    }

    public TemporalAdjuster getPastAdjuster() {
        return topLevelAdjusters.past;
    }

    public static ArrayList<ScheduledField> morePreciseThan(ScheduledField field) {
        ScheduledField[] all = values();
        ArrayList<ScheduledField> fields = new ArrayList<>(all.length);
        for (ScheduledField other : all) {
            if (other.compareTo(field) < 0) {
                fields.add(other);
            }
        }
        return fields;
    }
}
