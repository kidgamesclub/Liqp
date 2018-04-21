package liqp.nodes;

import lombok.Getter;

@Getter
public class OrNode extends ExpressionNode {

  public OrNode(LNode lhs, LNode rhs) {
    super(lhs, rhs);
  }

  @Override
  public Object render(RenderContext context) {
    return context.isTrue(lhs.render(context)) || context.isTrue(rhs.render(context));
  }
}
