package liqp.nodes;

import liqp.tags.CustomTag;
import liqp.tags.Tag;
import lombok.Getter;

@Getter
public class TagNode implements LNode {

  private Tag tag;
  private LNode[] tokens;

  public TagNode(Tag tag, LNode... tokens) {
    this(tag.name, tag, tokens);
  }

  public TagNode(String tagName, Tag tag, LNode... tokens) {
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

  @Override
  public Object render(RenderContext context) {
    return tag.render(context, tokens);
  }
}
