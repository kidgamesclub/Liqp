package liqp.tags;

import liqp.tag.LTag;
import liqp.node.LNode;

public abstract class CustomTag extends LTag {

  protected CustomTag(String name) {
    super(name);
  }

  /**
   * This method creates an instance tag based on the nodes in the tree (typically, tags are singletons at the template
   * level).  This allows for any optimizations or parsing to be done up-front, instead of on each render iteration.
   */
  public abstract LTag createTagForNode(LNode... tokens);
}
