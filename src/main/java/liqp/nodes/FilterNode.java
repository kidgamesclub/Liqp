package liqp.nodes;

import java.util.ArrayList;
import java.util.List;
import liqp.filters.Filter;
import liqp.filters.LFilter;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;

@Getter
public class FilterNode implements LNode {

  private final int line;
  private final int tokenStartIndex;
  private final String text;
  private final LFilter filter;
  private final List<LNode> params;

  public FilterNode(ParserRuleContext context, Filter filter) {
    this(context.start.getLine(), context.start.getCharPositionInLine(), context.getText(), filter);
  }

  private FilterNode(int line, int tokenStartIndex, String text, Filter filter) {

    if (filter == null) {
      throw new IllegalArgumentException("error on line " + line + ", index " + tokenStartIndex + ": no filter " +
            "available named: " + text);
    }

    this.line = line;
    this.tokenStartIndex = tokenStartIndex;
    this.text = text;
    this.filter = filter;
    this.params = new ArrayList<>();
  }

  public void add(LNode param) {
    params.add(param);
  }

  @Override
  public Object render(RenderContext context) {
    throw new RuntimeException("cannot render a filter");
  }
}
