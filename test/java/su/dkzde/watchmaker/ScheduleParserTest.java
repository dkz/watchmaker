package su.dkzde.watchmaker;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.testng.Assert;
import org.testng.annotations.Test;
import su.dkzde.watchmaker.parser.ScheduleGrammar;
import su.dkzde.watchmaker.parser.ScheduleNode;

/**
 * @author Dmitry Kozlov
 */
public class ScheduleParserTest {

    private static boolean parsed(String input) {
        ScheduleGrammar grammar = Parboiled.createParser(ScheduleGrammar.class);
        ParsingResult<ScheduleNode> result = new ReportingParseRunner<ScheduleNode>(grammar.Schedule()).run(input);
        return result.matched;
    }

    @Test public void parser() {
        Assert.assertFalse(parsed("{}"));
        Assert.assertFalse(parsed("{} - {}"));
        Assert.assertTrue(parsed("mon - sun"));
        Assert.assertTrue(parsed("{mon}, {tue}, {wed}, {thu}, {fri}"));
        Assert.assertFalse(parsed("14 - 15 - 16"));
        Assert.assertTrue(parsed("odd {10-14}, even {14-18}"));
        Assert.assertTrue(parsed("14:00"));
        Assert.assertTrue(parsed("10:00:30 - 14:00:30"));
        Assert.assertFalse(parsed("{!jan feb}"));
        Assert.assertTrue(parsed("!{jan} {feb}"));
        Assert.assertFalse(parsed("!{}"));
        Assert.assertFalse(parsed("!14"));
        Assert.assertTrue(parsed("mon 14:00 - wed 14:00"));
        Assert.assertFalse(parsed("!mon 14:00 - wed 14:00"));
    }
}
