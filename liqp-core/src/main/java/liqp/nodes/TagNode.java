package liqp.nodes;

import java.util.List;
import liqp.node.LNode;
import liqp.tags.CustomTag;
import liqp.tag.LTag;
import lombok.Getter;
import one.util.streamex.StreamEx;

@Getter
public class TagNode implements LNode {

  private LTag tag;
  private LNode[] tokens;

    public TagNode(LTag tag, List<LNode> tokens) {
        this(tag.getName(), tag, tokens.toArray(new LNode[tokens.size()]));
    }

    public TagNode(LTag tag, LNode... tokens) {
        this(tag.getName(), tag, tokens);
    }

  public TagNode(String tagName, LTag tag, LNode... tokens) {
    if (tagName == null) {
      throw new IllegalArgumentException("tagName == null");
    }
    if (tag == null) {
      throw new IllegalArgumentException("no tag available named: " + tagName);
    }

    if (tag instanceof CustomTag) {
      this.tag = ((CustomTag) tag).createTagForNode(tokens);
    } else {
      this.tag = tag;
    }

    this.tokens = tokens;
  }

  public LTag getTag() {
    return tag;
  }

  @Override
  public Object render(LContext context) {
    return tag.render(context, tokens);
  }

  @Override
  public List<LNode> children() {
    return StreamEx.of(tokens).toImmutableList();
  }
}
