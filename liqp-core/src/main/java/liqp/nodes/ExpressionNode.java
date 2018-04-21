package liqp.nodes;

import com.google.common.collect.ImmutableList;
import java.util.List;
import liqp.LValue;
import lombok.Getter;

@Getter
public abstract class ExpressionNode extends LValue implements LNode {
  public final LNode lhs;
  public final LNode rhs;

  protected ExpressionNode(LNode lhs, LNode rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }

  @Override
  public List<LNode> children() {
    return ImmutableList.of(lhs, rhs);
  }
}
