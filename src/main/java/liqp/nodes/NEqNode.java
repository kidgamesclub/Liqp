package liqp.nodes;

import liqp.LValue;
import lombok.Getter;

@Getter
public class NEqNode extends ExpressionNode {

  public NEqNode(LNode lhs, LNode rhs) {
    super(lhs, rhs);
  }

  @Override
  public Object render(RenderContext context) {

    Object a = lhs.render(context);
    Object b = rhs.render(context);

    return !LValue.areEqual(a, b);
  }
}
