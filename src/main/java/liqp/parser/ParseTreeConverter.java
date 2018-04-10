package liqp.parser;

import liqp.filters.Filters;
import liqp.nodes.LNode;
import liqp.parser.v4.NodeVisitor;
import liqp.tags.Tags;
import liquid.parser.v4.LiquidParser;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Annoying kotlin/antlr4 thing
 */
public class ParseTreeConverter {
  public LNode converTreeToLNode(LiquidParser parser,
                                 Tags tags,
                                 Filters filters,
                                 Flavor flavor,
                                 boolean isStrictVariables) {
    ParseTree tree = parser.parse();
    final NodeVisitor visitor = new NodeVisitor(tags, filters, flavor, isStrictVariables);
    return visitor.visit(tree);
  }
}
