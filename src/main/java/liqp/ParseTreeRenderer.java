package liqp;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;

public class ParseTreeRenderer {

  /**
   * Walks a (sub) tree of the root of the input source and builds a string representation of the structure of the parse
   * tree.
   * <p/>
   * Note that line breaks and multiple white space characters are trimmed to a single white space character.
   *
   * @param tree    the (sub) tree.
   * @param builder the StringBuilder to fill.
   */
  @SuppressWarnings("unchecked")
  private static void walk(ParseTree tree, StringBuilder builder) {

    List<ParseTree> firstStack = new ArrayList<ParseTree>();
    firstStack.add(tree);

    List<List<ParseTree>> childListStack = new ArrayList<List<ParseTree>>();
    childListStack.add(firstStack);

    while (!childListStack.isEmpty()) {

      List<ParseTree> childStack = childListStack.get(childListStack.size() - 1);

      if (childStack.isEmpty()) {
        childListStack.remove(childListStack.size() - 1);
      } else {
        tree = childStack.remove(0);

        String indent = "";

        for (int i = 0; i < childListStack.size() - 1; i++) {
          indent += (childListStack.get(i).size() > 0) ? "|  " : "   ";
        }

        String tokenName = tree.getClass().getSimpleName().replaceAll("Context$", "");
        String tokenText = tree.getText().replaceAll("\\s+", " ");

        builder.append(indent)
              .append(childStack.isEmpty() ? "'- " : "|- ")
              .append(tokenName)
              .append(tree.getChildCount() == 0 ? "='" + tokenText + "'" : "")
              .append("\n");

        if (tree.getChildCount() > 0) {
          childListStack.add(new ArrayList<ParseTree>(children(tree)));
        }
      }
    }
  }

  private static List<ParseTree> children(ParseTree parent) {

    List<ParseTree> children = new ArrayList<ParseTree>();

    for (int i = 0; i < parent.getChildCount(); i++) {
      children.add(parent.getChild(i));
    }

    return children;
  }

  /**
   * Returns a string representation of the parse tree of the parsed input source.
   *
   * @return a string representation of the parse tree of the parsed input source.
   */
  public static String renderParseTree(ParseTree parseTree) {

    StringBuilder builder = new StringBuilder();

    walk(parseTree, builder);

    return builder.toString();
  }
}
