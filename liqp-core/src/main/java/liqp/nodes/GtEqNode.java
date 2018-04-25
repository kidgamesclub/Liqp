package liqp.nodes;

import liqp.node.LNode;
import lombok.Getter;

@Getter
public class GtEqNode extends ExpressionNode{

  public GtEqNode(LNode lhs, LNode rhs) {
    super(lhs, rhs);
  }

  @Override
    public Object render(LContext context) {

        Object a = lhs.render(context);
        Object b = rhs.render(context);

        return (a instanceof Number) && (b instanceof Number) &&
                super.Companion.asNumber(a).doubleValue() >= super.Companion.asNumber(b).doubleValue();
    }
}
