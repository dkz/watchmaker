package su.dkzde.watchmaker.parser;

import org.parboiled.Rule;
import org.parboiled.support.Chars;
import org.parboiled.support.Var;

/**
 * @author Dmitry Kozlov
 */
public class ScheduleGrammar extends BaseScheduleParser {

    Rule Alphabetic() {
        return CharRange('a', 'z');
    }

    Rule Digit() {
        return CharRange('0', '9');
    }

    Rule Spacing() {
        return OneOrMore(AnyOf("\n\t "));
    }

    Rule TrailingSpaces(Rule rule) {
        return Sequence(Optional(Spacing()), rule, Optional(Spacing()));
    }

    Rule Comma() {
        return TrailingSpaces(fromCharLiteral(','));
    }

    Rule Hyphen() {
        return TrailingSpaces(fromCharLiteral('-'));
    }

    Rule Number() {
        return Sequence(
                OneOrMore(Digit()),
                push(new NumberNode(Integer.parseInt(match()), matchRange())));
    }

    Rule Identifier() {
        return Sequence(
                Sequence(Alphabetic(),
                        ZeroOrMore(FirstOf(Alphabetic(), Digit()))),
                push(new IdentifierNode(match(), matchRange())));
    }

    Rule Time() {
        Var<TimeNode.Builder> builder = new Var<>(new TimeNode.Builder());
        return Sequence(
                Number(), ACTION(builder.get().setHours(popNumberNode())),
                ':',
                Number(), ACTION(builder.get().setMinutes(popNumberNode())),
                Optional(
                        Sequence(':',
                                Number(), ACTION(builder.get().setSeconds(popNumberNode())))),
                push(builder.get().create()));
    }

    Rule Atom() {
        return FirstOf(Identifier(), Time(), Number());
    }

    Rule Element() {
        return Sequence(
                push(new ElementNode()),
                OneOrMore(TrailingSpaces(
                        Sequence(Atom(),
                                push(pop(1).addChild(pop()))))));
    }

    Rule Span() {
        Var<SpanNode> node = new Var<>(new SpanNode());
        return Sequence(
                Element(), ACTION(node.get().setFrom(pop())),
                Hyphen(),
                Element(), ACTION(node.get().setTill(pop())),
                push(node.get()));
    }

    Rule ElementOrSpan() {
        return FirstOf(Span(), Element());
    }

    Rule Parenthesis(Rule rule) {
        return Sequence('{', TrailingSpaces(rule), '}');
    }

    Rule Clause() {
        return FirstOf(
                Sequence('!', Parenthesis(OrClause()), push(new NotNode(pop()))),
                Parenthesis(OrClause()),
                ElementOrSpan());
    }

    Rule AndClause() {
        return Sequence(TrailingSpaces(Clause()),
                push(new AndClauseNode().addChild(pop())),
                Optional(Sequence(AndClause(),
                        push(pop().addChild(pop())))));
    }

    Rule OrClause() {
        return Sequence(
                AndClause(),
                push(new OrClauseNode().addChild(pop())),
                Optional(
                        Sequence(Comma(),
                        OrClause(),
                                push(pop().addChild(pop()))))

        );
    }

    public Rule Schedule() {
        return Sequence(TrailingSpaces(OrClause()), Chars.EOI);
    }
}
