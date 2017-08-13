package su.dkzde.watchmaker;

import org.testng.Assert;
import org.testng.annotations.Test;
import su.dkzde.watchmaker.core.DirectedAdjuster;
import su.dkzde.watchmaker.core.DirectedAdjusterCondition;
import su.dkzde.watchmaker.core.ScheduledField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import static su.dkzde.watchmaker.core.DirectedAdjuster.Direction.FUTURE;
import static su.dkzde.watchmaker.core.DirectedAdjuster.Direction.PAST;
import static su.dkzde.watchmaker.core.ScheduledField.*;

/**
 * @author Dmitry Kozlov
 */
public class DirectedAdjusterTest {

    private static class Condition {
        private long value;
        private ScheduledField field;
        private Condition(ScheduledField field, long value) {
            this.value = value;
            this.field = field;
        }
    }

    private static Condition with(long value, ScheduledField field) {
        return new Condition(field, value);
    }

    private static DirectedAdjuster newDirectedAdjusterTo(DirectedAdjuster.Direction direction, Condition... values) {
        ArrayList<DirectedAdjusterCondition> queries = new ArrayList<>(values.length);
        for (Condition value : values) {
            queries.add(new DirectedAdjusterCondition(value.field, value.value));
        }
        return DirectedAdjuster.to(direction, queries);
    }

    private static void testWithDirection(DirectedAdjuster.Direction direction, Condition... values) {
        LocalDateTime source = LocalDateTime.now();
        LocalDateTime result = source.with(newDirectedAdjusterTo(direction, values));
        switch (direction) {
            case FUTURE: {
                Assert.assertTrue(result.isAfter(source));
                break;
            }
            case PAST: {
                Assert.assertTrue(result.isBefore(source));
            }
        }
        for (Condition condition : values) {
            Assert.assertEquals(result.get(condition.field.getCorrespondingField()), condition.value);
        }
    }

    private static void testDirections(Condition... values) {
        testWithDirection(PAST, values);
        testWithDirection(FUTURE, values);
    }

    @Test void directedAdjusterProperties() {
        testDirections(with(1, WEEKDAY));
        testDirections(with(1, DAY));
        testDirections(with(1, WEEKDAY),
                with(1, DAY));
        testDirections(with(1, WEEKDAY),
                with(1, DAY),
                with(1, MONTH));
        testDirections(with(12, HOUR));
        testDirections(with(12, HOUR),
                with(30, MINUTE));
        testDirections(with(2, WEEKDAY),
                with(12, HOUR),
                with(30, MINUTE));
        testDirections(with(3, DAY),
                with(4, MONTH),
                with(5, WEEKDAY),
                with(12, HOUR),
                with(30, MINUTE));
    }

    private static LocalDateTime date(int year, Month month, int day) {
        return LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.MIN);
    }

    @Test void directedAdjusterPrecision() {
        LocalDateTime point = date(2017, Month.JULY, 17);
        Assert.assertEquals(
                point.with(newDirectedAdjusterTo(PAST, with(10, DAY), with(1, WEEKDAY))),
                date(2017, Month.JULY, 10));
        Assert.assertEquals(
                point.with(newDirectedAdjusterTo(PAST, with(11, DAY), with(4, WEEKDAY))),
                date(2017, Month.MAY, 11));
        Assert.assertEquals(
                point.with(newDirectedAdjusterTo(PAST, with(2, DAY), with(1, WEEKDAY))),
                date(2017, Month.JANUARY, 2));
    }
}
