package liqp

import liqp.nodes.LNode

/**
 * This class holds the state of a parsed template
 */
class Template(val rootNode: LNode, private val engine:TemplateEngine = TemplateEngine.newInstance()) {

  @JvmOverloads fun render(inputData: String, engine: TemplateEngine = this.engine): String {
    return engine.render(this, inputData)
  }

  @JvmOverloads fun render(inputData: Any? = null, engine: TemplateEngine = this.engine): String {
    return engine.render(this, inputData)
  }

  @JvmOverloads fun render(key: String, value: Any?, engine: TemplateEngine = this.engine): String {
    return engine.render(this, key to value)
  }

  //  /**
  //   * Returns a string representation of the parse tree of the parsed input source.
  //   *
  //   * @return a string representation of the parse tree of the parsed input source.
  //   */
  //  public String toStringTree() {
  //
  //    StringBuilder builder = new StringBuilder();
  //
  //    walk(root, builder);
  //
  //    return builder.toString();
  //  }

  ////  /**
  ////   * Walks a (sub) tree of the root of the input source and builds a string representation of the structure of the parse
  ////   * tree.
  ////   * <p/>
  ////   * Note that line breaks and multiple white space characters are trimmed to a single white space character.
  ////   *
  ////   * @param tree    the (sub) tree.
  ////   * @param builder the StringBuilder to fill.
  ////   */
  ////  @SuppressWarnings("unchecked")
  ////  private void walk(ParseTree tree, StringBuilder builder) {
  ////
  ////    List<ParseTree> firstStack = new ArrayList<ParseTree>();
  ////    firstStack.add(tree);
  ////
  ////    List<List<ParseTree>> childListStack = new ArrayList<List<ParseTree>>();
  ////    childListStack.add(firstStack);
  ////
  ////    while (!childListStack.isEmpty()) {
  ////
  ////      List<ParseTree> childStack = childListStack.get(childListStack.size() - 1);
  ////
  ////      if (childStack.isEmpty()) {
  ////        childListStack.remove(childListStack.size() - 1);
  ////      } else {
  ////        tree = childStack.remove(0);
  ////
  ////        String indent = "";
  ////
  ////        for (int i = 0; i < childListStack.size() - 1; i++) {
  ////          indent += (childListStack.get(i).size() > 0) ? "|  " : "   ";
  ////        }
  ////
  ////        String tokenName = tree.getClass().getSimpleName().replaceAll("Context$", "");
  ////        String tokenText = tree.getText().replaceAll("\\s+", " ");
  ////
  ////        builder.append(indent)
  ////              .append(childStack.isEmpty() ? "'- " : "|- ")
  ////              .append(tokenName)
  ////              .append(tree.getChildCount() == 0 ? "='" + tokenText + "'" : "")
  ////              .append("\n");
  ////
  ////        if (tree.getChildCount() > 0) {
  ////          childListStack.add(new ArrayList<>(children(tree)));
  ////        }
  ////      }
  ////    }
  //  }
} //  /**
//   * Returns the root of the parse tree of the parsed input.
//   *
//   * @return the root of the parse tree of the parsed input.
//   */
//  public ParseTree getParseTree() {
//    return root;
//  }
