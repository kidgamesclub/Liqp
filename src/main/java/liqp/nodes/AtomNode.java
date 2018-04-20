package liqp.nodes;

import java.util.Collections;
import java.util.List;

public class AtomNode implements LNode {

    public static final AtomNode EMPTY = new AtomNode(new Object());

    private Object value;

    public AtomNode(Object value) {
        this.value = value;
    }

    public static boolean isEmpty(Object o) {
        return o == EMPTY.value;
    }

    @Override
    public Object render(RenderContext context) {

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
