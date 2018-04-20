package liqp.nodes;

import liqp.LValue;
import lombok.Getter;

@Getter
public class OrNode extends ExpressionNode {

  public OrNode(LNode lhs, LNode rhs) {
    super(lhs, rhs);
  }

  @Override
    public Object render(RenderContext context) {

        return super.asBoolean(lhs.render(context)) || super.asBoolean(rhs.render(context));

    }
}
