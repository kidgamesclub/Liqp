package liqp.nodes;

import liqp.LValue;
import lombok.Getter;

@Getter
public class EqNode extends ExpressionNode{

  public EqNode(LNode lhs, LNode rhs) {
    super(lhs, rhs);
  }

  @Override
    public Object render(RenderContext context) {

        Object a = lhs.render(context);
        Object b = rhs.render(context);

        return LValue.areEqual(a, b);

    }
}
