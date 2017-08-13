package su.dkzde.watchmaker;

import org.testng.Assert;
import org.testng.annotations.Test;
import su.dkzde.watchmaker.core.ScheduledField;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.HashMap;

/**
 * @author Dmitry Kozlov
 */
public class ScheduleTest {

    private static ResolverContext context = new ResolverContext(
            new MapNamespaceResolver(
                    new HashMap<String, ResolvedElement>() {{
                        put("mon", new ResolvedScheduleCondition(ScheduledField.WEEKDAY, 1));
                        put("tue", new ResolvedScheduleCondition(ScheduledField.WEEKDAY, 2));
                        put("wed", new ResolvedScheduleCondition(ScheduledField.WEEKDAY, 3));
                        put("thu", new ResolvedScheduleCondition(ScheduledField.WEEKDAY, 4));
                        put("fri", new ResolvedScheduleCondition(ScheduledField.WEEKDAY, 5));
                        put("sat", new ResolvedScheduleCondition(ScheduledField.WEEKDAY, 6));
                        put("sun", new ResolvedScheduleCondition(ScheduledField.WEEKDAY, 7));

                        put("jan", new ResolvedScheduleCondition(ScheduledField.MONTH, 1));
                        put("feb", new ResolvedScheduleCondition(ScheduledField.MONTH, 2));
                        put("mar", new ResolvedScheduleCondition(ScheduledField.MONTH, 3));
                        put("apr", new ResolvedScheduleCondition(ScheduledField.MONTH, 4));
                        put("may", new ResolvedScheduleCondition(ScheduledField.MONTH, 5));
                        put("jun", new ResolvedScheduleCondition(ScheduledField.MONTH, 6));
                        put("jul", new ResolvedScheduleCondition(ScheduledField.MONTH, 7));
                        put("aug", new ResolvedScheduleCondition(ScheduledField.MONTH, 8));
                        put("sep", new ResolvedScheduleCondition(ScheduledField.MONTH, 9));
                        put("oct", new ResolvedScheduleCondition(ScheduledField.MONTH, 10));
                        put("nov", new ResolvedScheduleCondition(ScheduledField.MONTH, 11));
                        put("dec", new ResolvedScheduleCondition(ScheduledField.MONTH, 12));

                        put("odd", new ResolvedScheduleComponent(date ->
                                1 == date.get(ChronoField.DAY_OF_MONTH) % 2));
                        put("even", new ResolvedScheduleComponent(date ->
                                0 == date.get(ChronoField.DAY_OF_MONTH) % 2));
                    }}
            ),
            new DayAndYearNumberResolver());

    private static void areSatisfiedBy(String specification, LocalDateTime... dates) {
        Schedule schedule = Schedules.resolve(specification, context);
        for (LocalDateTime date : dates) {
            Assert.assertTrue(schedule.isSatisfied(date));
        }
    }

    private static void areNotSatisfiedBy(String specification, LocalDateTime... dates) {
        Schedule schedule = Schedules.resolve(specification, context);
        for (LocalDateTime date : dates) {
            Assert.assertFalse(schedule.isSatisfied(date));
        }
    }

    @Test public void testSchedule() {
        String regular = "{mon, tue, wed} 19:00 - 20:00";
        areSatisfiedBy(regular,
                LocalDateTime.of(2017, Month.AUGUST, 14, 19, 30),
                LocalDateTime.of(2017, Month.AUGUST, 15, 19, 30),
                LocalDateTime.of(2017, Month.AUGUST, 16, 19, 30));
        areNotSatisfiedBy(regular,
                LocalDateTime.of(2017, Month.AUGUST, 14, 20, 30),
                LocalDateTime.of(2017, Month.AUGUST, 13, 18, 30));

        String evensAndOdds = "odd {10:00 - 14:00}, even {14:00 - 18:00}";
        areSatisfiedBy(evensAndOdds,
                LocalDateTime.of(2017, Month.AUGUST, 13, 11, 30),
                LocalDateTime.of(2017, Month.AUGUST, 14, 16, 0),
                LocalDateTime.of(2017, Month.AUGUST, 15, 13, 0),
                LocalDateTime.of(2017, Month.AUGUST, 16, 14, 30));
        areNotSatisfiedBy(evensAndOdds,
                LocalDateTime.of(2017, Month.AUGUST, 13, 14, 30),
                LocalDateTime.of(2017, Month.AUGUST, 14, 13, 30),
                LocalDateTime.of(2017, Month.AUGUST, 15, 19, 0),
                LocalDateTime.of(2017, Month.AUGUST, 16, 9, 0));
    }
}
