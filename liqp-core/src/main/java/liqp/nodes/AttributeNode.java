package liqp.nodes;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.Getter;

@Getter
public class AttributeNode implements LNode {

    private final LNode key;
    private final LNode value;

    public AttributeNode(LNode key, LNode value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Object render(RenderContext context) {
        return new Object[]{
                key.render(context),
                value.render(context)
        };
    }

  @Override
  public List<LNode> children() {
    return ImmutableList.of(key, value);
  }
}
