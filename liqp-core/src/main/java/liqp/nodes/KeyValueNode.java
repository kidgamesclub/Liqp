package liqp.nodes;

import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import liqp.node.LNode;
import lombok.Getter;

@Getter
public class KeyValueNode implements LNode {

    public final String key;
    public final LNode value;

    public KeyValueNode(String key, LNode value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Object render(LContext context) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value.render(context));
        return map;
    }

  @Override
  public List<LNode> children() {
    return ImmutableList.of(value);
  }
}
