package liqp.nodes;

import liqp.node.LNode;
import liqp.node.LValue;
import lombok.Getter;

@Getter
public class EqNode extends ExpressionNode{

  public EqNode(LNode lhs, LNode rhs) {
    super(lhs, rhs);
  }

  @Override
    public Object render(LContext context) {

        Object a = lhs.render(context);
        Object b = rhs.render(context);

        return LValue.Companion.areEqual(a, b);

    }
}
