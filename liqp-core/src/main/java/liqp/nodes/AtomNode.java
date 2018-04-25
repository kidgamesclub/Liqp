package liqp.nodes;

import java.util.Collections;
import java.util.List;
import liqp.context.LContext;
import liqp.node.LNode;

public class AtomNode implements LNode {

    private Object value;

    public AtomNode(Object value) {
        this.value = value;
    }


    @Override
    public Object render(LContext context) {

        return value;
    }

    @SuppressWarnings("unchecked")
    public <X> X get() {
      return (X) value;
    }

  @Override
  public List<LNode> children() {
    return Collections.emptyList();
  }
}
