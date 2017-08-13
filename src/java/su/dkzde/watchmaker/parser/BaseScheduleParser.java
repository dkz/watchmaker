package su.dkzde.watchmaker.parser;

import org.parboiled.BaseParser;

/**
 * @author Dmitry Kozlov
 */
public abstract class BaseScheduleParser extends BaseParser<ScheduleNode> {

    protected NumberNode popNumberNode() {
        return (NumberNode) pop();
    }
}
