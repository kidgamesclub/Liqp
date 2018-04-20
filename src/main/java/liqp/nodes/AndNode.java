package liqp.nodes;

import java.util.List;
import lombok.Getter;

@Getter
public class AndNode extends ExpressionNode {

  public AndNode(LNode lhs, LNode rhs) {
    super(lhs, rhs);
  }

  @Override
  public Object render(RenderContext context) {
    return super.asBoolean(lhs.render(context)) && super.asBoolean(rhs.render(context));
  }
}
