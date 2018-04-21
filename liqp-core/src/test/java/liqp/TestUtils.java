package liqp;

import liqp.filters.Filters;
import liqp.nodes.LNode;
import liqp.parser.Flavor;
import liqp.parser.v4.NodeVisitor;
import liqp.tags.Tags;
import liquid.parser.v4.LiquidLexer;
import liquid.parser.v4.LiquidParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public final class TestUtils {

    private TestUtils() {
        // no need to instantiate this class
    }

    /**
     * Parses the input `source` and invokes the `rule` and returns this LNode.
     *
     * @param source
     *              the input source to be parsed.
     * @param rule
     *              the rule name (method name) to be invoked
     * @return
     * @throws Exception
     */
    public static LNode getNode(String source, String rule) throws Exception {
        return getNode(source, rule, false, Flavor.LIQUID);
    }

    public static LNode getNode(String source, String rule, boolean strictVariables, Flavor flavor) throws Exception {

        LiquidLexer lexer = new LiquidLexer(CharStreams.fromString("{{ " + source + " }}"));
        LiquidParser parser =  new LiquidParser(new CommonTokenStream(lexer));

        LiquidParser.OutputContext root = parser.output();
        NodeVisitor visitor = new NodeVisitor(Tags.getDefaultTags(), Filters.getDefaultFilters(), flavor,
              strictVariables);

        return visitor.visitOutput(root);
    }

    public static Throwable getExceptionRootCause(Throwable e) {
        Throwable cause;
        Throwable result = e;

        while (null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }
}
