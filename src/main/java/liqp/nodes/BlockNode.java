package liqp.nodes;

import static liqp.LValue.BREAK;
import static liqp.LValue.CONTINUE;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class BlockNode implements LNode {

    private List<LNode> children;
    private final boolean isRootBlock;

    public BlockNode() {
        this(false);
    }

    public BlockNode(boolean isRootBlock) {
        this.children = new ArrayList<LNode>();
        this.isRootBlock = isRootBlock;
    }

    public void add(LNode node) {
        children.add(node);
    }

    public List<LNode> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public Object render(RenderContext context) {

        StringBuilder builder = new StringBuilder();

        for (LNode node : children) {

            Object value = node.render(context);

            if(value == null) {
                continue;
            }

            if(value == BREAK || value == CONTINUE) {
                return value;
            }
            else if (value instanceof List) {

                List list = (List) value;

                for (Object obj : list) {
                    builder.append(String.valueOf(obj));
                }
            }
            else if (value.getClass().isArray()) {

                Object[] array = (Object[]) value;

                for (Object obj : array) {
                    builder.append(String.valueOf(obj));
                }
            }
            else {
                builder.append(String.valueOf(value));
            }

            if (builder.length() > context.getMaxSizeRenderedString()) {
                throw new RuntimeException("rendered string exceeds " + context.getMaxSizeRenderedString());
            }
        }

        return builder.toString();
    }

  @Override
  public List<LNode> children() {
    return children;
  }
}
